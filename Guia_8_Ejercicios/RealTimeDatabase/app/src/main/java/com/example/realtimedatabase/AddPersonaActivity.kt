package com.example.realtimedatabase

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.realtimedatabase.datos.Persona
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/** Actividad con doble función: agregar ("a") o editar ("e") una persona. */
class AddPersonaActivity : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtDUI: EditText
    private lateinit var edtFechaNacimiento: EditText
    private lateinit var rgGenero: RadioGroup
    private lateinit var edtPeso: EditText
    private lateinit var edtAltura: EditText

    private var key: String = ""
    private var accion: String = ""
    private lateinit var database: DatabaseReference

    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        isLenient = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_persona)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        inicializar()
    }

    private fun inicializar() {
        edtNombre = findViewById(R.id.edtNombre)
        edtDUI = findViewById(R.id.edtDUI)
        edtFechaNacimiento = findViewById(R.id.edtFechaNacimiento)
        rgGenero = findViewById(R.id.rgGenero)
        edtPeso = findViewById(R.id.edtPeso)
        edtAltura = findViewById(R.id.edtAltura)

        edtFechaNacimiento.setOnClickListener { mostrarSelectorFecha() }

        // Datos enviados por la actividad anterior
        intent.extras?.let {
            key = it.getString("key", "")
            accion = it.getString("accion", "")
            edtNombre.setText(it.getString("nombre", ""))
            edtDUI.setText(it.getString("dui", ""))
            edtFechaNacimiento.setText(it.getString("fechaNacimiento", ""))
            edtPeso.setText(it.getString("peso", ""))
            edtAltura.setText(it.getString("altura", ""))
            when (it.getString("genero", "")) {
                "Masculino" -> findViewById<RadioButton>(R.id.rbMasculino).isChecked = true
                "Femenino" -> findViewById<RadioButton>(R.id.rbFemenino).isChecked = true
                "Otro" -> findViewById<RadioButton>(R.id.rbOtro).isChecked = true
            }
        }
        title = if (accion == "e") "Editar persona" else "Nueva persona"
    }

    private fun mostrarSelectorFecha() {
        val cal = Calendar.getInstance()
        val actual = edtFechaNacimiento.text.toString()
        if (actual.isNotEmpty()) {
            try {
                formatoFecha.parse(actual)?.let { cal.time = it }
            } catch (_: Exception) { }
        }
        val dialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                val sel = Calendar.getInstance().apply { set(year, month, day) }
                edtFechaNacimiento.setText(formatoFecha.format(sel.time))
            },
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        dialog.datePicker.maxDate = System.currentTimeMillis() // No permite fechas futuras
        dialog.show()
    }

    private fun generoSeleccionado(): String? = when (rgGenero.checkedRadioButtonId) {
        R.id.rbMasculino -> "Masculino"
        R.id.rbFemenino -> "Femenino"
        R.id.rbOtro -> "Otro"
        else -> null
    }

    /** Se ejecuta con android:onClick="guardar" del botón Guardar. */
    fun guardar(v: View?) {
        val nombre = edtNombre.text.toString().trim()
        val dui = edtDUI.text.toString().trim()
        val fecha = edtFechaNacimiento.text.toString().trim()
        val genero = generoSeleccionado()
        val peso = edtPeso.text.toString().replace(',', '.').toDoubleOrNull()
        val altura = edtAltura.text.toString().replace(',', '.').toDoubleOrNull()

        // ---- Validaciones ----
        if (nombre.isEmpty()) { edtNombre.error = "Ingrese el nombre"; return }
        if (dui.isEmpty()) { edtDUI.error = "Ingrese el DUI"; return }
        if (fecha.isEmpty()) { edtFechaNacimiento.error = "Seleccione la fecha"; return }
        if (genero == null) {
            Toast.makeText(this, "Seleccione el género", Toast.LENGTH_SHORT).show(); return
        }
        if (peso == null || peso <= 0 || peso > 500) {
            edtPeso.error = "Peso válido en kg (ej. 70.5)"; return
        }
        if (altura == null || altura < 0.3 || altura > 2.8) {
            edtAltura.error = "Altura válida en metros (ej. 1.75)"; return
        }

        database = FirebaseDatabase.getInstance().getReference("personas")
        val persona = Persona(
            dui = dui,
            nombre = nombre,
            fechaNacimiento = fecha,
            genero = genero,
            peso = peso,
            altura = altura
        )

        if (accion == "a") { // Agregar registro
            val newKey = database.push().key // Genera una nueva clave
            if (newKey != null) {
                database.child(newKey).setValue(persona.toMap()).addOnSuccessListener {
                    Toast.makeText(this, "Se guardó con éxito", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No se pudo generar una clave", Toast.LENGTH_SHORT).show()
                return
            }
        } else if (accion == "e") { // Editar registro
            if (key.isNotEmpty()) {
                val childUpdates = hashMapOf<String, Any?>(key to persona.toMap())
                database.updateChildren(childUpdates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Se actualizó con éxito", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "No se encontró la clave del registro", Toast.LENGTH_SHORT).show()
                return
            }
        }
        finish()
    }

    /** Se ejecuta con android:onClick="cancelar" del botón Cancelar. */
    fun cancelar(v: View?) {
        finish()
    }
}
