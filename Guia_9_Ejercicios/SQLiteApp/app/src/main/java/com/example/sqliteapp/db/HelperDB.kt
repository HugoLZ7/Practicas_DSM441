package com.example.sqliteapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqliteapp.model.Categoria
import com.example.sqliteapp.model.Productos
import com.example.sqliteapp.model.Usuario

/**
 * SQLiteOpenHelper de la aplicación (base de datos "tienda.sqlite").
 * Se usa un único helper (singleton) para evitar abrir varias conexiones al mismo archivo.
 */
class HelperDB private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "tienda.sqlite"
        private const val DB_VERSION = 1

        @Volatile
        private var instance: HelperDB? = null

        fun getInstance(context: Context): HelperDB =
            instance ?: synchronized(this) {
                instance ?: HelperDB(context).also { instance = it }
            }
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        // SQLite no valida llaves foráneas por defecto: se habilita explícitamente
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Categoria.CREATE_TABLE_CATEGORIA)
        db.execSQL(Productos.CREATE_TABLE_PRODUCTOS)
        db.execSQL(Usuario.CREATE_TABLE_USUARIO)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Sin migraciones por el momento (DB_VERSION = 1).
        // Ejemplo: if (oldVersion < 2) db.execSQL("ALTER TABLE ...")
    }
}
