package com.example.sqliteapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteapp.model.Usuario
import com.example.sqliteapp.util.aplicarInsetsSistema

/** Primera pantalla de la aplicación: inicio de sesión. */
class LoginActivity : AppCompatActivity() {

    private lateinit var edtNick: EditText
    private lateinit var edtPassword: EditText
    private lateinit var usuario: Usuario

    // Si el usuario se registra, regresa con su nick para precargarlo en el formulario
    private val registroLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                edtNick.setText(result.data?.getStringExtra("nick") ?: "")
                edtPassword.setText("")
                edtPassword.requestFocus()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<android.view.View>(R.id.main).aplicarInsetsSistema()

        usuario = Usuario(this)
        edtNick = findViewById(R.id.edtNick)
        edtPassword = findViewById(R.id.edtPassword)

        findViewById<Button>(R.id.btnIngresar).setOnClickListener { ingresar() }
        findViewById<Button>(R.id.btnRegistrarse).setOnClickListener {
            registroLauncher.launch(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun ingresar() {
        val nick = edtNick.text.toString().trim()
        val password = edtPassword.text.toString()

        if (nick.isEmpty()) { edtNick.error = "Ingrese su nick"; edtNick.requestFocus(); return }
        if (password.isEmpty()) { edtPassword.error = "Ingrese su contraseña"; edtPassword.requestFocus(); return }

        if (usuario.autenticar(nick, password)) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nick", nick)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Nick o contraseña incorrectos", Toast.LENGTH_LONG).show()
        }
    }
}
