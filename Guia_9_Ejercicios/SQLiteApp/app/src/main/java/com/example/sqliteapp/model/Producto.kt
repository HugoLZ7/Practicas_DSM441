package com.example.sqliteapp.model

/** Fila de la tabla productos. */
data class Producto(
    val id: Int,
    val idcategoria: Int,
    val descripcion: String,
    val precio: Double,
    val cantidad: Int
)
