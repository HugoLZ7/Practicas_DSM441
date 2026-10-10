package com.example.sqliteapp.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import com.example.sqliteapp.db.HelperDB

class Categoria(context: Context) {

    private val db: SQLiteDatabase = HelperDB.getInstance(context).writableDatabase

    companion object {
        // TABLA CATEGORIA
        const val TABLE_NAME_CATEGORIA = "categoria"

        // Nombre de los campos de la tabla
        const val COL_ID = "idcategoria"
        const val COL_NOMBRE = "nombre"

        // Sentencia SQL para crear la tabla
        const val CREATE_TABLE_CATEGORIA = (
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CATEGORIA + "("
                        + COL_ID + " integer primary key autoincrement,"
                        + COL_NOMBRE + " varchar(50) NOT NULL);"
                )
    }

    // ContentValues
    fun generarContentValues(nombre: String?): ContentValues {
        val valores = ContentValues()
        valores.put(COL_NOMBRE, nombre)
        return valores
    }

    /** Inserta las categorías por defecto solo si la tabla está vacía (primera instalación). */
    fun insertValuesDefault() {
        val categories = arrayOf(
            "Abarrotes", "Carnes", "Embutidos", "Mariscos", "Pescado",
            "Bebidas", "Verduras", "Frutas", "Bebidas Carbonatadas", "Bebidas no carbonatadas"
        )
        if (DatabaseUtils.queryNumEntries(db, TABLE_NAME_CATEGORIA) == 0L) {
            db.beginTransaction()
            try {
                for (item in categories) {
                    db.insert(TABLE_NAME_CATEGORIA, null, generarContentValues(item))
                }
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }
    }

    fun showAllCategoria(): Cursor {
        val columns = arrayOf(COL_ID, COL_NOMBRE)
        return db.query(TABLE_NAME_CATEGORIA, columns, null, null, null, null, "$COL_NOMBRE ASC")
    }

    /** Lista de nombres de categorías (ordenadas alfabéticamente) para el Spinner. */
    fun nombres(): List<String> {
        val lista = ArrayList<String>()
        showAllCategoria().use { c ->
            while (c.moveToNext()) lista.add(c.getString(1))
        }
        return lista
    }

    // El Spinner solo guarda el nombre: esta función recupera el ID de la categoría
    fun searchID(nombre: String): Int? {
        val columns = arrayOf(COL_ID, COL_NOMBRE)
        db.query(
            TABLE_NAME_CATEGORIA, columns,
            "$COL_NOMBRE=?", arrayOf(nombre), null, null, null
        ).use { c ->
            return if (c.moveToFirst()) c.getInt(0) else null
        }
    }

    fun searchNombre(id: Int): String? {
        val columns = arrayOf(COL_ID, COL_NOMBRE)
        db.query(
            TABLE_NAME_CATEGORIA, columns,
            "$COL_ID=?", arrayOf(id.toString()), null, null, null
        ).use { c ->
            return if (c.moveToFirst()) c.getString(1) else null
        }
    }
}
