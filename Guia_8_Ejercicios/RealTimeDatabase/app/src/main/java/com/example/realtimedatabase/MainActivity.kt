package com.example.realtimedatabase

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.realtimedatabase.datos.Persona
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    // Ordenamiento para hacer las consultas a los datos
    private val consultaOrdenada: Query = refPersonas.orderByChild("nombre")
    private var personas: MutableList<Persona> = ArrayList()
    private var listaPersonas: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        inicializar()
    }

    private fun inicializar() {
        val fabAgregar: FloatingActionButton = findViewById(R.id.fab_agregar)
        listaPersonas = findViewById(R.id.ListaPersonas)

        // Clic en un elemento de la lista -> editar registro
        listaPersonas!!.setOnItemClickListener { _, _, i, _ ->
            val persona = personas[i]
            val intent = Intent(this, AddPersonaActivity::class.java)
            intent.putExtra("accion", "e") // Editar
            intent.putExtra("key", persona.key)
            intent.putExtra("nombre", persona.nombre)
            intent.putExtra("dui", persona.dui)
            intent.putExtra("fechaNacimiento", persona.fechaNacimiento)
            intent.putExtra("genero", persona.genero)
            intent.putExtra("peso", persona.peso?.toString() ?: "")
            intent.putExtra("altura", persona.altura?.toString() ?: "")
            startActivity(intent)
        }

        // Clic sostenido -> eliminar registro (con confirmación)
        listaPersonas!!.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, position, _ ->
                val ad = AlertDialog.Builder(this@MainActivity)
                ad.setMessage("¿Está seguro de eliminar el registro?")
                    .setTitle("Confirmación")
                ad.setPositiveButton("Sí") { _, _ ->
                    personas[position].key?.let { refPersonas.child(it).removeValue() }
                    Toast.makeText(this@MainActivity, "Registro borrado!", Toast.LENGTH_SHORT).show()
                }
                ad.setNegativeButton("No") { _, _ ->
                    Toast.makeText(
                        this@MainActivity,
                        "Operación de borrado cancelada!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ad.show()
                true
            }

        // Botón (+) -> agregar registro
        fabAgregar.setOnClickListener {
            val intent = Intent(this, AddPersonaActivity::class.java)
            intent.putExtra("accion", "a") // Agregar
            intent.putExtra("key", "")
            intent.putExtra("nombre", "")
            intent.putExtra("dui", "")
            intent.putExtra("fechaNacimiento", "")
            intent.putExtra("genero", "")
            intent.putExtra("peso", "")
            intent.putExtra("altura", "")
            startActivity(intent)
        }

        // Escucha en tiempo real: se ejecuta en cada cambio de la base de datos
        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                personas.clear()
                for (dato in dataSnapshot.children) {
                    val persona: Persona? = dato.getValue(Persona::class.java)
                    persona?.key = dato.key // Clave generada por Firebase
                    if (persona != null) {
                        personas.add(persona)
                    }
                }
                listaPersonas!!.adapter = PersonaAdapter(this@MainActivity, personas)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Error al leer datos: ${databaseError.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    companion object {
        private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        private val refPersonas: DatabaseReference = database.getReference("personas")
    }
}
