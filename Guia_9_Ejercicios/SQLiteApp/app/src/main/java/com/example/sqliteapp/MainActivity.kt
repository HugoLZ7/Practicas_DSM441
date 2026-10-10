package com.example.sqliteapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteapp.model.Categoria
import com.example.sqliteapp.model.Producto
import com.example.sqliteapp.model.Productos
import com.example.sqliteapp.util.aplicarInsetsSistema
import java.util.Locale

/** Administración de productos: CRUD sobre la tabla productos (SQLite). */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var managerCategoria: Categoria
    private lateinit var managerProductos: Productos

    private lateinit var tvUsuario: TextView
    private lateinit var txtIdDB: TextView
    private lateinit var txtId: EditText
    private lateinit var txtNombre: EditText
    private lateinit var txtPrecio: EditText
    private lateinit var txtCantidad: EditText
    private lateinit var cmbCategorias: Spinner

    private var categorias: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.main).aplicarInsetsSistema()

        tvUsuario = findViewById(R.id.tvUsuario)
        txtIdDB = findViewById(R.id.txtIdDB)
        txtId = findViewById(R.id.txtId)
        txtNombre = findViewById(R.id.txtNombre)
        txtPrecio = findViewById(R.id.txtPrecio)
        txtCantidad = findViewById(R.id.txtCantidad)
        cmbCategorias = findViewById(R.id.cmbCategorias)

        managerCategoria = Categoria(this)
        managerProductos = Productos(this)

        tvUsuario.text = "Sesión: ${intent.getStringExtra("nick") ?: ""}"

        setSpinnerCategorias()

        for (id in listOf(
            R.id.btnAgregar, R.id.btnActualizar, R.id.btnEliminar, R.id.btnBuscar,
            R.id.btnListar, R.id.btnLimpiar, R.id.btnCerrarSesion
        )) {
            findViewById<Button>(id).setOnClickListener(this)
        }
    }

    private fun setSpinnerCategorias() {
        // Carga las categorías por defecto solo la primera vez
        managerCategoria.insertValuesDefault()
        categorias = managerCategoria.nombres()

        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cmbCategorias.adapter = adaptador
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnAgregar -> agregar()
            R.id.btnActualizar -> actualizar()
            R.id.btnEliminar -> confirmarEliminar()
            R.id.btnBuscar -> buscar()
            R.id.btnListar -> listarTodos()
            R.id.btnLimpiar -> limpiarFormulario()
            R.id.btnCerrarSesion -> cerrarSesion()
        }
    }

    // ---------------------------------------------------------------- CREATE
    private fun agregar() {
        if (!verificarFormulario("insertar")) return
        val idcategoria = idCategoriaSeleccionada() ?: return

        val resultado = managerProductos.addNewProducto(
            idcategoria,
            txtNombre.text.toString().trim(),
            leerPrecio(),
            txtCantidad.text.toString().trim().toInt()
        )
        if (resultado != -1L) {
            Toast.makeText(this, "Producto agregado (código $resultado)", Toast.LENGTH_LONG).show()
            limpiarFormulario()
        } else {
            Toast.makeText(this, "No se pudo agregar el producto", Toast.LENGTH_LONG).show()
        }
    }

    // ---------------------------------------------------------------- UPDATE
    private fun actualizar() {
        if (!verificarFormulario("actualizar")) return
        val idcategoria = idCategoriaSeleccionada() ?: return

        val filas = managerProductos.updateProducto(
            txtId.text.toString().trim().toInt(),
            idcategoria,
            txtNombre.text.toString().trim(),
            leerPrecio(),
            txtCantidad.text.toString().trim().toInt()
        )
        if (filas > 0) {
            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_LONG).show()
            limpiarFormulario()
        } else {
            Toast.makeText(this, "No existe un producto con ese código", Toast.LENGTH_LONG).show()
        }
    }

    // ---------------------------------------------------------------- DELETE
    private fun confirmarEliminar() {
        if (!verificarFormulario("eliminar")) return
        val id = txtId.text.toString().trim().toInt()

        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage("¿Está seguro de eliminar el producto con código $id?")
            .setPositiveButton("Sí") { _, _ ->
                if (managerProductos.deleteProducto(id) > 0) {
                    Toast.makeText(this, "Producto eliminado", Toast.LENGTH_LONG).show()
                    limpiarFormulario()
                } else {
                    Toast.makeText(this, "No existe un producto con ese código", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    // ------------------------------------------------------------------ READ
    /** Botón Buscar: localiza un producto por su código y llena el formulario. */
    private fun buscar() {
        if (!verificarFormulario("buscar")) return
        val id = txtId.text.toString().trim().toInt()

        val producto = managerProductos.getProducto(id)
        if (producto != null) {
            cargarEnFormulario(producto)
            Toast.makeText(this, "Producto encontrado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se encontró un producto con el código $id", Toast.LENGTH_LONG).show()
        }
    }

    /** Muestra todos los productos; al tocar uno se carga en el formulario. */
    private fun listarTodos() {
        val lista = managerProductos.getAll()
        if (lista.isEmpty()) {
            Toast.makeText(this, "No hay productos registrados", Toast.LENGTH_LONG).show()
            return
        }
        val items = lista.map {
            "${it.id} · ${it.descripcion} — ${formatoPrecio(it.precio)} (x${it.cantidad})"
        }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Productos (${lista.size})")
            .setItems(items) { _, i -> cargarEnFormulario(lista[i]) }
            .setNegativeButton("Cerrar", null)
            .show()
    }

    // --------------------------------------------------------------- AYUDAS
    private fun cargarEnFormulario(p: Producto) {
        txtIdDB.text = p.id.toString()
        txtId.setText(p.id.toString())
        txtNombre.setText(p.descripcion)
        txtPrecio.setText(String.format(Locale.US, "%.2f", p.precio))
        txtCantidad.setText(p.cantidad.toString())
        val nombreCategoria = managerCategoria.searchNombre(p.idcategoria)
        val pos = categorias.indexOf(nombreCategoria)
        if (pos >= 0) cmbCategorias.setSelection(pos)
    }

    private fun limpiarFormulario() {
        txtIdDB.text = ""
        txtId.text.clear()
        txtNombre.text.clear()
        txtPrecio.text.clear()
        txtCantidad.text.clear()
        cmbCategorias.setSelection(0)
        txtId.error = null; txtNombre.error = null; txtPrecio.error = null; txtCantidad.error = null
        txtNombre.requestFocus()
    }

    private fun cerrarSesion() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun idCategoriaSeleccionada(): Int? {
        val nombre = cmbCategorias.selectedItem?.toString()?.trim().orEmpty()
        val id = managerCategoria.searchID(nombre)
        if (id == null) {
            Toast.makeText(this, "Seleccione una categoría válida", Toast.LENGTH_LONG).show()
        }
        return id
    }

    private fun leerPrecio(): Double =
        txtPrecio.text.toString().trim().replace(',', '.').toDouble()

    private fun formatoPrecio(p: Double) = String.format(Locale.US, "$%.2f", p)

    /** Valida los campos según la operación: insertar | actualizar | eliminar | buscar. */
    private fun verificarFormulario(opc: String): Boolean {
        var ok = true
        var notificacion = "Se han generado algunos errores, favor verifíquelos"

        if (opc == "insertar" || opc == "actualizar") {
            if (txtNombre.text.toString().trim().isEmpty()) {
                txtNombre.error = "Ingrese el nombre del producto"; txtNombre.requestFocus(); ok = false
            }
            val precio = txtPrecio.text.toString().trim().replace(',', '.').toDoubleOrNull()
            if (precio == null || precio < 0) {
                txtPrecio.error = "Ingrese un precio válido (ej. 1.50)"; txtPrecio.requestFocus(); ok = false
            }
            val cantidad = txtCantidad.text.toString().trim().toIntOrNull()
            if (cantidad == null || cantidad < 0) {
                txtCantidad.error = "Ingrese una cantidad válida (0 o más)"; txtCantidad.requestFocus(); ok = false
            }
        }
        if (opc != "insertar") {
            if (txtId.text.toString().trim().toIntOrNull() == null) {
                txtId.error = "Ingrese el código del producto"
                txtId.requestFocus()
                notificacion = "No se ha seleccionado un producto"
                ok = false
            }
        }
        if (!ok) Toast.makeText(this, notificacion, Toast.LENGTH_LONG).show()
        return ok
    }
}
