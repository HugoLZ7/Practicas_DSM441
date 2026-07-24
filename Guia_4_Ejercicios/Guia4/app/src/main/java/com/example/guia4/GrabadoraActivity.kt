package com.example.guia4

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class GrabadoraActivity : AppCompatActivity() {

    private lateinit var btnIniciarGrabacion: Button
    private lateinit var txtEstado: TextView

    // Lanzador de la solicitud de permiso (Activity Result API)
    private val solicitarPermiso = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            txtEstado.text = "Grabando"
            btnIniciarGrabacion.isEnabled = true
        } else {
            txtEstado.text = "Permiso denegado"
            btnIniciarGrabacion.isEnabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grabadora)

        btnIniciarGrabacion = findViewById(R.id.btnIniciarGrabacion)
        txtEstado = findViewById(R.id.txtEstado)

        // 1. Al iniciar, comprobar el permiso RECORD_AUDIO
        if (permisoConcedido()) {
            txtEstado.text = "Esperando permiso" // botón visible y habilitado
            btnIniciarGrabacion.isEnabled = true
        } else {
            btnIniciarGrabacion.isEnabled = true // se habilita para poder solicitar
            solicitarPermiso.launch(Manifest.permission.RECORD_AUDIO)
        }

        // 2. Click en "Iniciar Grabación"
        btnIniciarGrabacion.setOnClickListener {
            if (permisoConcedido()) {
                txtEstado.text = "Grabando"
            } else {
                solicitarPermiso.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun permisoConcedido(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
}