package com.example.sqliteapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteapp.model.Usuario
import com.example.sqliteapp.util.aplicarInsetsSistema

/** Registro de nuevos usuarios (tabla usuario). */
class RegisterActivity : AppCompatActivity() {

    private lateinit var edtNick: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmar: EditText
    private lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        findViewById<android.view.View>(R.id.main).aplicarInsetsSistema()

        usuario = Usuario(this)
        edtNick = findViewById(R.id.edtNickRegistro)
        edtPassword = findViewById(R.id.edtPasswordRegistro)
        edtConfirmar = findViewById(R.id.edtConfirmarPassword)

        findViewById<Button>(R.id.btnCrearCuenta).setOnClickListener { registrar() }
        findViewById<Button>(R.id.btnCancelarRegistro).setOnClickListener { finish() }
    }

    private fun registrar() {
        val nick = edtNick.text.toString().trim()
        val password = edtPassword.text.toString()
        val confirmar = edtConfirmar.text.toString()

        if (!Regex("^[A-Za-z0-9_.-]{3,30}$").matches(nick)) {
            edtNick.error = "3 a 30 caracteres: letras, números, _ . -"
            edtNick.requestFocus(); return
        }
        if (password.length < 6) {
            edtPassword.error = "La contraseña debe tener al menos 6 caracteres"
            edtPassword.requestFocus(); return
        }
        if (password != confirmar) {
            edtConfirmar.error = "Las contraseñas no coinciden"
            edtConfirmar.requestFocus(); return
        }

        if (usuario.registrar(nick, password)) {
            Toast.makeText(this, "Cuenta creada. Ya puede iniciar sesión", Toast.LENGTH_LONG).show()
            setResult(Activity.RESULT_OK, Intent().putExtra("nick", nick))
            finish()
        } else {
            edtNick.error = "Ese nick ya está en uso"
            edtNick.requestFocus()
        }
    }
}
