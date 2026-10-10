package com.example.sqliteapp.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.sqliteapp.db.HelperDB
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/** Tabla "usuario": nick + contraseña (se guarda el hash PBKDF2 con sal, nunca la contraseña en texto plano). */
class Usuario(context: Context) {

    private val db: SQLiteDatabase = HelperDB.getInstance(context).writableDatabase

    companion object {
        const val TABLE_NAME_USUARIO = "usuario"
        const val COL_ID = "idusuario"
        const val COL_NICK = "nick"
        const val COL_PASSWORD = "password"
        const val COL_SALT = "salt"

        const val CREATE_TABLE_USUARIO = (
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USUARIO + "("
                        + COL_ID + " integer primary key autoincrement,"
                        + COL_NICK + " varchar(30) NOT NULL UNIQUE COLLATE NOCASE,"
                        + COL_PASSWORD + " varchar(100) NOT NULL,"
                        + COL_SALT + " varchar(40) NOT NULL);"
                )
    }

    fun existeNick(nick: String): Boolean {
        db.query(
            TABLE_NAME_USUARIO, arrayOf(COL_ID),
            "$COL_NICK=?", arrayOf(nick), null, null, null
        ).use { return it.moveToFirst() }
    }

    /** Registra un usuario. Devuelve false si el nick ya existe o si falló la inserción. */
    fun registrar(nick: String, password: String): Boolean {
        if (existeNick(nick)) return false
        val salt = PasswordHasher.newSalt()
        val valores = ContentValues().apply {
            put(COL_NICK, nick)
            put(COL_PASSWORD, PasswordHasher.hash(password, salt))
            put(COL_SALT, salt)
        }
        return db.insert(TABLE_NAME_USUARIO, null, valores) != -1L
    }

    /** Verifica nick y contraseña. */
    fun autenticar(nick: String, password: String): Boolean {
        db.query(
            TABLE_NAME_USUARIO, arrayOf(COL_PASSWORD, COL_SALT),
            "$COL_NICK=?", arrayOf(nick), null, null, null
        ).use { c ->
            if (!c.moveToFirst()) return false
            val guardado = c.getString(0)
            val salt = c.getString(1)
            val calculado = PasswordHasher.hash(password, salt)
            return MessageDigest.isEqual(guardado.toByteArray(), calculado.toByteArray())
        }
    }
}

private object PasswordHasher {
    private const val ITERATIONS = 10_000
    private const val KEY_LENGTH = 256

    fun newSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return toHex(bytes)
    }

    fun hash(password: String, saltHex: String): String {
        val spec = PBEKeySpec(password.toCharArray(), fromHex(saltHex), ITERATIONS, KEY_LENGTH)
        val bytes = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).encoded
        return toHex(bytes)
    }

    private fun toHex(bytes: ByteArray) = bytes.joinToString("") { "%02x".format(it) }

    private fun fromHex(hex: String) =
        ByteArray(hex.length / 2) { hex.substring(it * 2, it * 2 + 2).toInt(16).toByte() }
}
