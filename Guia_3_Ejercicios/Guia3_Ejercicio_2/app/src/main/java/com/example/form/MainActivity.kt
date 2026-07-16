package com.example.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var etNombres: EditText
    lateinit var etApellidos: EditText
    lateinit var etCorreo: EditText
    lateinit var etTelefono: EditText
    lateinit var etEdad: EditText
    lateinit var btnValidar: Button
    lateinit var tvResumen: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etNombres = findViewById(R.id.etNombres)
        etApellidos = findViewById(R.id.etApellidos)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
        etEdad = findViewById(R.id.etEdad)
        btnValidar = findViewById(R.id.btnValidar)
        tvResumen = findViewById(R.id.tvResumen)

        lifecycle.addObserver(MyLifeCycleObserver("MainActivity"))

        btnValidar.setOnClickListener {
            validarInformacion()
        }

        // Restaurar estado ante cambio de configuración
        if (savedInstanceState != null) {
            etNombres.setText(savedInstanceState.getString("NOMBRES", ""))
            etApellidos.setText(savedInstanceState.getString("APELLIDOS", ""))
            etCorreo.setText(savedInstanceState.getString("CORREO", ""))
            etTelefono.setText(savedInstanceState.getString("TELEFONO", ""))
            etEdad.setText(savedInstanceState.getString("EDAD", ""))
            tvResumen.text = savedInstanceState.getString("RESUMEN", "")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("NOMBRES", etNombres.text.toString())
        outState.putString("APELLIDOS", etApellidos.text.toString())
        outState.putString("CORREO", etCorreo.text.toString())
        outState.putString("TELEFONO", etTelefono.text.toString())
        outState.putString("EDAD", etEdad.text.toString())
        outState.putString("RESUMEN", tvResumen.text.toString())
    }

    private fun validarInformacion() {
        val nombres = etNombres.text.toString().trim()
        val apellidos = etApellidos.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val edadTexto = etEdad.text.toString().trim()

        if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() ||
            telefono.isEmpty() || edadTexto.isEmpty()) {
            tvResumen.text = "Error: todos los campos son obligatorios."
            return
        }

        val edad = edadTexto.toIntOrNull()
        if (edad == null) {
            tvResumen.text = "Error: la edad debe ser un valor numérico."
            return
        }

        val mayorEdad = edad >= 18
        val estadoEdad = if (mayorEdad) "Mayor de edad" else "Menor de edad"

        tvResumen.text = """
            Resumen de la información ingresada
            ------------------------------------
            Nombres: $nombres
            Apellidos: $apellidos
            Correo: $correo
            Teléfono: $telefono
            Edad: $edad ($estadoEdad)
        """.trimIndent()
    }
}