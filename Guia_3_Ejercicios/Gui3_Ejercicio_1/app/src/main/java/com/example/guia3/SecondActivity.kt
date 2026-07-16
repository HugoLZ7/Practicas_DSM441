package com.example.guia3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    lateinit var btnRegresar: Button
    lateinit var btnTercera: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        btnRegresar = findViewById(R.id.btnRegresar)
        btnTercera = findViewById(R.id.btnTercera)

        lifecycle.addObserver(MyLifeCycleObserver("SecondActivity"))

        btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnTercera.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

        mostrarToast("onCreate")
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
        Toast.makeText(this, "SecondActivity -> $mensaje", duracion).show()
    }
}