package com.example.realtimedatabase.datos

import com.google.firebase.database.Exclude

/**
 * Modelo de una persona almacenada en Realtime Database (nodo "personas").
 * Todos los campos tienen valor por defecto: Firebase necesita un constructor sin argumentos.
 */
data class Persona(
    var dui: String? = null,
    var nombre: String? = null,
    var fechaNacimiento: String? = null,   // formato dd/MM/yyyy
    var genero: String? = null,            // Masculino | Femenino | Otro
    var peso: Double? = null,              // kilogramos
    var altura: Double? = null,            // metros
    @get:Exclude var key: String? = null,  // clave generada por Firebase (no se guarda como campo)
    var per: MutableMap<String, Boolean> = mutableMapOf()
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "dui" to dui,
            "nombre" to nombre,
            "fechaNacimiento" to fechaNacimiento,
            "genero" to genero,
            "peso" to peso,
            "altura" to altura,
            "per" to per
        )
    }
}
