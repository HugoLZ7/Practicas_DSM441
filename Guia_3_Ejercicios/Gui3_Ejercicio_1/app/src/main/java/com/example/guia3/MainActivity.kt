package com.example.guia3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var btnAbrir: Button
    lateinit var btnTercera: Button
    lateinit var btnIncrementar: Button
    lateinit var tvContador: TextView
    var contador: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Referencia al archivo layout
        setContentView(R.layout.activity_main)

        // 2. Referencias a las vistas
        btnAbrir = findViewById(R.id.btnAbrir)
        btnTercera = findViewById(R.id.btnTercera)
        btnIncrementar = findViewById(R.id.btnIncrementar)
        tvContador = findViewById(R.id.tvContador)

        // 3. Registro del observador
        lifecycle.addObserver(MyLifeCycleObserver("MainActivity"))

        // 4. Listener para abrir SecondActivity
        btnAbrir.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        // 5. Listener para abrir ThirdActivity (Ejercicio 1)
        btnTercera.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

        // 6. Listener del botón incrementar
        btnIncrementar.setOnClickListener {
            tvContador.text = "Valor contador: ${++contador}"
        }

        // 7. Recuperar estado
        if (savedInstanceState != null) {
            contador = savedInstanceState.getInt("CONTADOR")
            tvContador.text = "Valor contador: $contador"
        }

        mostrarToast("onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("CONTADOR", contador)
    }

    override fun onStart() {
        super.onStart()
        mostrarToast("onStart")
    }

    override fun onResume() {
        super.onResume()
        mostrarToast("onResume")
    }

    override fun onPause() {
        super.onPause()
        mostrarToast("onPause")
    }

    override fun onStop() {
        super.onStop()
        mostrarToast("onStop")
    }

    override fun onRestart() {
        super.onRestart()
        mostrarToast("onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        mostrarToast("onDestroy")
    }

    private fun mostrarToast(mensaje: String, duracion: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, "MainActivity -> $mensaje", duracion).show()
    }
}