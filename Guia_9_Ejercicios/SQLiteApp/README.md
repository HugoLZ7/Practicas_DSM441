# SQLiteApp — Almacenamiento de datos con SQLite

**Universidad Don Bosco · Facultad de Ingeniería · Escuela de Computación**
**Materia:** DSM104 – Desarrollo de Software para Móviles · **Ciclo:** 02-2026
**Guía de laboratorio:** Almacenamiento de datos con SQLite

Aplicación Android (Kotlin, vistas XML) que usa **SQLiteOpenHelper** para administrar productos de una tienda, con **inicio de sesión y registro de usuarios**.

## Funcionalidades

- **Login** como primera pantalla (nick + contraseña) y **registro** de nuevos usuarios.
- **CRUD de productos:** Agregar · Actualizar · Eliminar · **Buscar** (por código).
- Extras: *Ver todos* (lista y carga el producto al tocarlo), *Limpiar* y *Cerrar sesión*.
- Categorías precargadas la primera vez que se abre la app (10 categorías).

## Cumplimiento de la guía (Sección V)

| Actividad | Implementación |
|-----------|----------------|
| 1. Botón **Buscar** | `MainActivity.buscar()` busca por código y llena el formulario (incluida la categoría en el Spinner). |
| 2. Revisar **Actualizar** y **Eliminar** | Corregidos (ver abajo). |
| 3. **Login** con SQLite como primera pantalla | `LoginActivity` + `RegisterActivity`; `AndroidManifest.xml` la declara como `LAUNCHER`. |
| 3.b Tabla **usuario** (≥ 2 campos) | `usuario(idusuario, nick, password, salt)`; la contraseña se guarda como hash **PBKDF2** con sal, nunca en texto plano. |

### Correcciones respecto al código base de la guía

- El Spinner repetía la primera categoría (`add` antes del `do-while`).
- `Productos(this)` abría un `HelperDB` nuevo en cada clic → ahora `HelperDB` es *singleton*.
- Comparación de cadenas con `===` (`opc === "insertar"`) → ahora `==`/`when`.
- `toDouble()`/`toInt()` podían lanzar excepción con datos inválidos → `toDoubleOrNull()`/`toIntOrNull()` con validación.
- Actualizar/Eliminar mostraban "éxito" aunque el código no existiera → ahora se verifica el número de filas afectadas.
- Eliminar pide confirmación y el formulario se limpia tras cada operación.
- `searchID`/`searchNombre` fallaban con cursor vacío y no cerraban el cursor → ahora usan `use {}` y devuelven `null`.
- Se habilitan las llaves foráneas (`setForeignKeyConstraintsEnabled(true)`), que SQLite no valida por defecto.
- Layout reemplazado por `ScrollView` + `LinearLayout` (mismos `id`) para que no se corte en pantallas pequeñas.

## Estructura

```
app/src/main/java/com/example/sqliteapp/
├── LoginActivity.kt        # Primera pantalla
├── RegisterActivity.kt     # Alta de usuarios
├── MainActivity.kt         # CRUD de productos
├── db/HelperDB.kt          # SQLiteOpenHelper (tienda.sqlite)
├── model/
│   ├── Categoria.kt        # Tabla categoria + categorías por defecto
│   ├── Productos.kt        # Tabla productos + operaciones CRUD
│   ├── Producto.kt         # Clase de datos de una fila
│   └── Usuario.kt          # Tabla usuario + registro/autenticación
└── util/Ui.kt              # Insets de barras del sistema
docs/esquema.sql            # Esquema SQL de la base de datos
```

### Modelo de datos

```
categoria (idcategoria PK, nombre)
   │ 1
   │
   │ N
productos (idproductos PK, idcategoria FK, descripcion, precio, cantidad)

usuario (idusuario PK, nick UNIQUE, password, salt)
```

## Cómo ejecutarlo

**Requisitos:** Android Studio (versión reciente), dispositivo/emulador con API 24+.

1. Clona el repositorio y ábrelo en Android Studio (se sincroniza Gradle automáticamente).
2. Ejecuta la app (▶). No requiere configuración adicional ni internet.
3. Pulsa **Crear cuenta**, regístrate e inicia sesión.

### Inspeccionar la base de datos (pasos 11–18 de la guía)

1. Android Studio → **View > Tool Windows > Device Explorer**.
2. Ruta: `data/data/com.example.sqliteapp/databases/tienda.sqlite` → clic derecho → **Save As…**
3. Abre el archivo con [DB Browser for SQLite](https://sqlitebrowser.org/dl/).

## Tecnologías

Kotlin · Android Views (XML) · Material Components · SQLite (`SQLiteOpenHelper`) · Gradle Kotlin DSL (AGP 8.7.3)

## Autor

Hugo Lopez Rivera — Universidad Don Bosco
