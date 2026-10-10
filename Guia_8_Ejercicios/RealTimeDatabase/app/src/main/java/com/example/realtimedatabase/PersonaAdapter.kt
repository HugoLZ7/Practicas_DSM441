package com.example.realtimedatabase

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.realtimedatabase.datos.Persona

class PersonaAdapter(private val context: Activity, var personas: List<Persona>) :
    ArrayAdapter<Persona>(context, R.layout.persona_layout, personas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Método invocado tantas veces como elementos tenga la colección personas
        val rowView: View = convertView
            ?: context.layoutInflater.inflate(R.layout.persona_layout, parent, false)

        val tvNombre = rowView.findViewById<TextView>(R.id.tvNombre)
        val tvDUI = rowView.findViewById<TextView>(R.id.tvDUI)
        val tvFecha = rowView.findViewById<TextView>(R.id.tvFechaNacimiento)
        val tvGenero = rowView.findViewById<TextView>(R.id.tvGenero)
        val tvMedidas = rowView.findViewById<TextView>(R.id.tvMedidas)

        val persona = personas[position]

        tvNombre.text = "Nombre: ${persona.nombre ?: "—"}"
        tvDUI.text = "DUI: ${persona.dui ?: "—"}"
        tvFecha.text = "Fecha de nacimiento: ${persona.fechaNacimiento ?: "—"}"
        tvGenero.text = "Género: ${persona.genero ?: "—"}"
        val peso = persona.peso?.let { "%.1f kg".format(it) } ?: "—"
        val altura = persona.altura?.let { "%.2f m".format(it) } ?: "—"
        tvMedidas.text = "Peso: $peso   |   Altura: $altura"

        return rowView
    }
}
