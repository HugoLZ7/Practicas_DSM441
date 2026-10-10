# RealTimeDatabase — CRUD de personas con Firebase Realtime Database

**Universidad Don Bosco · Facultad de Ingeniería · Escuela de Computación**
**Materia:** DSM104 – Desarrollo de Software para Móviles · **Ciclo:** 02-2026
**Guía de laboratorio:** #9 – Realtime Database

Aplicación Android (Kotlin, vistas XML) que realiza un **CRUD completo** sobre Firebase Realtime Database.
Los cambios se sincronizan en tiempo real: cualquier modificación en la base de datos se refleja al instante en la lista.

## Funcionalidades

| Operación | Cómo se usa |
|-----------|-------------|
| **Create** | Botón **(+)** → formulario → *Guardar* |
| **Read**   | `MainActivity` muestra la lista ordenada por nombre con `addValueEventListener` |
| **Update** | Tocar un elemento de la lista → editar → *Guardar* |
| **Delete** | Mantener presionado un elemento → confirmar en el diálogo |

### Discusión de resultados (Sección IV de la guía)
Se amplió el modelo `Persona` con los campos solicitados:

- **Fecha de nacimiento** — selector de fecha (`DatePickerDialog`), formato `dd/MM/yyyy`, no admite fechas futuras.
- **Género** — `RadioGroup` (Masculino / Femenino / Otro).
- **Peso** — kg, numérico decimal (validado 0–500).
- **Altura** — metros, numérico decimal (validado 0.30–2.80).

Se agregaron validaciones de campos obligatorios y formato antes de guardar.

## Estructura del proyecto

```
app/src/main/
├── java/com/example/realtimedatabase/
│   ├── MainActivity.kt          # Lista de personas, eliminar, ir a agregar/editar
│   ├── AddPersonaActivity.kt    # Agregar / editar (doble función)
│   ├── PersonaAdapter.kt        # Adaptador personalizado de la lista
│   └── datos/Persona.kt         # Modelo de datos
└── res/
    ├── layout/ (activity_main, activity_add_persona, persona_layout)
    └── values/ (colors, themes, strings)
```

Estructura de datos en Firebase:

```json
{
  "personas": {
    "-OYtGEoFF8WGdHhHfbBL": {
      "dui": "01020304-9",
      "nombre": "Jhon Doe",
      "fechaNacimiento": "15/03/2000",
      "genero": "Masculino",
      "peso": 70.5,
      "altura": 1.75
    }
  }
}
```

## Cómo ejecutarlo

**Requisitos:** Android Studio (versión reciente), JDK 17+ (incluido con Android Studio), dispositivo/emulador con API 24+.

1. Clona el repositorio y ábrelo en Android Studio.
2. En [Firebase Console](https://console.firebase.google.com/) crea el proyecto **FirebaseDBRealTime** y una **Realtime Database** (ubicación `us-central1`, modo de prueba).
3. Registra una app Android con el paquete `com.example.realtimedatabase` (o usa **Tools > Firebase > Realtime Database > Connect to Firebase**).
4. Descarga `google-services.json` y colócalo en `app/google-services.json`.
   *(Está en `.gitignore`; se incluye `google-services.json.example` como referencia.)*
5. En la pestaña **Reglas** de la base de datos pega el contenido de `database.rules.json` y pulsa **Publicar**.
6. Sincroniza Gradle y ejecuta la app (▶).

## ⚠️ Seguridad

Las reglas `".read": true, ".write": true` se usan **solo con fines académicos** (según la guía): cualquier persona con la URL de la base puede leer y borrar los datos. Para un uso real, agrega Firebase Authentication y reglas como `".read": "auth != null"`.

## Tecnologías

Kotlin · Android Views (XML) · Material Components · Firebase Realtime Database (BoM 33.7.0) · Gradle Kotlin DSL (AGP 8.7.3)

## Autor

Hugo Lopez Rivera — Universidad Don Bosco
