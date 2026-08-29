package com.example.registrousuario

import android.os.Bundle
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val rgGenero = findViewById<RadioGroup>(R.id.rgGenero)
        val cbTerminos = findViewById<android.widget.CheckBox>(R.id.cbTerminos)
        val btnRegistrar = findViewById<android.widget.Button>(R.id.btnRegistrar)
        val btnCancelar = findViewById<android.widget.Button>(R.id.btnCancelar)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()
            val generoId = rgGenero.checkedRadioButtonId

            when {
                nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty() -> {
                    Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                }
                generoId == -1 -> {
                    Toast.makeText(this, "Selecciona un género", Toast.LENGTH_SHORT).show()
                }
                !cbTerminos.isChecked -> {
                    Toast.makeText(this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val genero = findViewById<android.widget.RadioButton>(generoId).text
                    Toast.makeText(
                        this,
                        "Registro exitoso:\n$nombre $apellido\n$correo\nGénero: $genero",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        btnCancelar.setOnClickListener {
            etNombre.text.clear()
            etApellido.text.clear()
            etCorreo.text.clear()
            etContrasena.text.clear()
            rgGenero.clearCheck()
            cbTerminos.isChecked = false
        }
    }
}