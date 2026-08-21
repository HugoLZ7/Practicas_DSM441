package com.example.guia5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Activity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        val btnVolver = findViewById<android.widget.Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // regresa al Activity principal (MainActivity)
        }
    }
}