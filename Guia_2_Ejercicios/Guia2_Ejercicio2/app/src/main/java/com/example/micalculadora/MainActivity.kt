package com.example.micalculadora

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var edt1: EditText
    private lateinit var edt2: EditText
    private lateinit var txtResultado: TextView
    private lateinit var txtHistorial: TextView

    private val historial = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edt1 = findViewById(R.id.edtNumero1)
        edt2 = findViewById(R.id.edtNumero2)

        txtResultado = findViewById(R.id.txtResultado)
        txtHistorial = findViewById(R.id.txtHistorial)

        findViewById<Button>(R.id.btnSumar).setOnClickListener {
            operar("+")
        }

        findViewById<Button>(R.id.btnRestar).setOnClickListener {
            operar("-")
        }

        findViewById<Button>(R.id.btnMultiplicar).setOnClickListener {
            operar("*")
        }

        findViewById<Button>(R.id.btnDividir).setOnClickListener {
            operar("/")
        }

        findViewById<Button>(R.id.btnPorcentaje).setOnClickListener {

            if (edt1.text.toString().isEmpty()) {
                Toast.makeText(this, "Ingrese un número", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val n = edt1.text.toString().toDouble()
            val r = n / 100

            mostrarResultado("$n % = $r")
        }

        findViewById<Button>(R.id.btnPotencia).setOnClickListener {

            if (edt1.text.toString().isEmpty()) {
                Toast.makeText(this, "Ingrese un número", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val n = edt1.text.toString().toDouble()
            val r = n * n

            mostrarResultado("$n² = $r")
        }

        findViewById<Button>(R.id.btnRaiz).setOnClickListener {

            if (edt1.text.toString().isEmpty()) {
                Toast.makeText(this, "Ingrese un número", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val n = edt1.text.toString().toDouble()

            if (n < 0) {
                Toast.makeText(this, "No existe raíz de números negativos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val r = sqrt(n)

            mostrarResultado("√$n = $r")
        }

        findViewById<Button>(R.id.btnLimpiar).setOnClickListener {

            edt1.setText("")
            edt2.setText("")
            txtResultado.text = "Resultado:"
            txtHistorial.text = "Historial"

            historial.clear()
        }
    }

    private fun operar(op: String) {

        if (edt1.text.toString().isEmpty() || edt2.text.toString().isEmpty()) {
            Toast.makeText(this, "Ingrese ambos números", Toast.LENGTH_SHORT).show()
            return
        }

        val a = edt1.text.toString().toDouble()
        val b = edt2.text.toString().toDouble()

        val resultado = when (op) {
            "+" -> a + b
            "-" -> a - b
            "*" -> a * b
            "/" -> {
                if (b == 0.0) {
                    Toast.makeText(this, "No se puede dividir entre cero", Toast.LENGTH_SHORT).show()
                    return
                }
                a / b
            }
            else -> 0.0
        }

        mostrarResultado("$a $op $b = $resultado")
    }

    private fun mostrarResultado(texto: String) {

        txtResultado.text = "Resultado: $texto"

        historial.add(0, texto)

        if (historial.size > 5) {
            historial.removeAt(5)
        }

        txtHistorial.text = "Historial\n\n" + historial.joinToString("\n")
    }
}