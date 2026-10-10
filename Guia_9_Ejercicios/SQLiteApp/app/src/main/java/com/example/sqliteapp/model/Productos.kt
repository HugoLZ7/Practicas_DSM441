package com.example.sqliteapp.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.sqliteapp.db.HelperDB

class Productos(context: Context) {

    private val db: SQLiteDatabase = HelperDB.getInstance(context).writableDatabase

    companion object {
        // TABLA PRODUCTOS
        const val TABLE_NAME_PRODUCTOS = "productos"

        // Nombre de los campos de la tabla
        const val COL_ID = "idproductos"
        const val COL_IDCATEGORIA = "idcategoria"
        const val COL_DESCRIPCION = "descripcion"
        const val COL_PRECIO = "precio"
        const val COL_CANTIDAD = "cantidad"

        // Sentencia SQL para crear la tabla
        const val CREATE_TABLE_PRODUCTOS = (
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_PRODUCTOS + "("
                        + COL_ID + " integer primary key autoincrement,"
                        + COL_IDCATEGORIA + " integer NOT NULL,"
                        + COL_DESCRIPCION + " varchar(150) NOT NULL,"
                        + COL_PRECIO + " decimal(10,2) NOT NULL,"
                        + COL_CANTIDAD + " integer,"
                        + "FOREIGN KEY(" + COL_IDCATEGORIA + ") REFERENCES "
                        + Categoria.TABLE_NAME_CATEGORIA + "(" + Categoria.COL_ID + "));"
                )

        private val COLUMNS = arrayOf(COL_ID, COL_IDCATEGORIA, COL_DESCRIPCION, COL_PRECIO, COL_CANTIDAD)
    }

    // ContentValues
    fun generarContentValues(
        idcategoria: Int?,
        descripcion: String?,
        precio: Double?,
        cantidad: Int?
    ): ContentValues {
        val valores = ContentValues()
        valores.put(COL_IDCATEGORIA, idcategoria)
        valores.put(COL_DESCRIPCION, descripcion)
        valores.put(COL_PRECIO, precio)
        valores.put(COL_CANTIDAD, cantidad)
        return valores
    }

    /** Agrega un nuevo registro. Devuelve el id generado o -1 si falló. */
    fun addNewProducto(idcategoria: Int?, descripcion: String?, precio: Double?, cantidad: Int?): Long {
        return db.insert(
            TABLE_NAME_PRODUCTOS, null,
            generarContentValues(idcategoria, descripcion, precio, cantidad)
        )
    }

    /** Elimina un registro. Devuelve el número de filas eliminadas (0 si no existía). */
    fun deleteProducto(id: Int): Int {
        return db.delete(TABLE_NAME_PRODUCTOS, "$COL_ID=?", arrayOf(id.toString()))
    }

    /** Modifica un registro. Devuelve el número de filas actualizadas (0 si no existía). */
    fun updateProducto(
        id: Int,
        idcategoria: Int?,
        descripcion: String?,
        precio: Double?,
        cantidad: Int?
    ): Int {
        return db.update(
            TABLE_NAME_PRODUCTOS,
            generarContentValues(idcategoria, descripcion, precio, cantidad),
            "$COL_ID=?", arrayOf(id.toString())
        )
    }

    // Mostrar un registro particular (Cursor)
    fun searchProducto(id: Int): Cursor {
        return db.query(
            TABLE_NAME_PRODUCTOS, COLUMNS,
            "$COL_ID=?", arrayOf(id.toString()), null, null, null
        )
    }

    // Mostrar todos los registros (Cursor)
    fun searchProductosAll(): Cursor {
        return db.query(TABLE_NAME_PRODUCTOS, COLUMNS, null, null, null, null, "$COL_DESCRIPCION ASC")
    }

    /** Busca un producto por código y lo devuelve como objeto (null si no existe). */
    fun getProducto(id: Int): Producto? {
        searchProducto(id).use { c ->
            return if (c.moveToFirst()) fromCursor(c) else null
        }
    }

    /** Devuelve todos los productos ordenados por descripción. */
    fun getAll(): List<Producto> {
        val lista = ArrayList<Producto>()
        searchProductosAll().use { c ->
            while (c.moveToNext()) lista.add(fromCursor(c))
        }
        return lista
    }

    private fun fromCursor(c: Cursor) = Producto(
        id = c.getInt(c.getColumnIndexOrThrow(COL_ID)),
        idcategoria = c.getInt(c.getColumnIndexOrThrow(COL_IDCATEGORIA)),
        descripcion = c.getString(c.getColumnIndexOrThrow(COL_DESCRIPCION)),
        precio = c.getDouble(c.getColumnIndexOrThrow(COL_PRECIO)),
        cantidad = c.getInt(c.getColumnIndexOrThrow(COL_CANTIDAD))
    )
}
