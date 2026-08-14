# 🔬 Física de Bolsillo (Pocket Physics Lab)

Convierte el teléfono o la tableta en un pequeño laboratorio para explorar
fenómenos físicos reales, usando los sensores que ya trae el dispositivo.
Diseñado para niñas y niños de **8 a 12 años**, en español, **100 %
offline**.

- **Repositorio:** `pocket-physics-lab-android`
- **Package:** `com.kidslab.pocketphysics`
- **Versión:** 1.0.0
- **minSdk:** 24 · **Kotlin + Jetpack Compose + Material 3 + Room + MVVM**

## ✨ Qué incluye la versión 1.0.0

- **9 pantallas**: Científico de bolsillo, Mis sensores, 4 laboratorios
  (movimiento, rotación, sonido, luz), Experimentos guiados, Registro
  científico, y Desafíos/logros.
- **17 experimentos guiados** (más de los 15 mínimos pedidos), cada uno con
  Pregunta → Predicción → Medición → Resultado → Conclusión.
- **4 sensores explorados**: acelerómetro, giroscopio, micrófono y sensor
  de luz (opcional, con detección automática de disponibilidad).
- **6 desafíos** con **6 insignias** coleccionables.
- **Seguridad física** integrada en el propio diseño de cada experimento:
  nunca se pide lanzar, dejar caer, mojar, acercar al fuego, conectar a
  electricidad, usar en un vehículo en movimiento, subir a alturas ni
  golpear el dispositivo.
- **Privacidad**: sin permiso de Internet, un único permiso sensible
  (`RECORD_AUDIO`, solo para el laboratorio de sonido), sin audio guardado
  en disco, sin ubicación, cámara ni contactos.

## 📁 Estructura del repositorio

```
pocket-physics-lab-android/
├── app/                     # Código fuente Android (Kotlin + Compose)
├── database/                # schema.sql, sample_data.sql
├── docs/                    # Documentación en Markdown + PDFs
│   └── pdf/                 # MEMORIA_DESCRIPTIVA.pdf, MANUAL_USUARIO.pdf, MANUAL_TECNICO.pdf
├── gradle/                  # Gradle wrapper
├── .github/workflows/       # CI: tests, build, docs, release
├── settings.gradle.kts
├── build.gradle.kts
├── gradlew / gradlew.bat
└── SUBIR_A_GITHUB.md        # Guía para publicar este proyecto en GitHub
```

## 📚 Documentación

- [Memoria descriptiva](docs/MEMORIA_DESCRIPTIVA.md) — qué es, objetivo educativo, seguridad, sensores, privacidad, limitaciones.
- [Manual de usuario](docs/MANUAL_USUARIO.md) — guía para niñas, niños y familias.
- [Manual técnico](docs/MANUAL_TECNICO.md) — arquitectura, capas, pruebas, compilación.
- [Base de datos](docs/BASE_DE_DATOS.md) — esquema completo con diagrama entidad-relación (Mermaid).

## 🧪 Pruebas

Todas las pruebas corren en la JVM (con Robolectric cuando se necesita
`Context`), **sin hardware real ni emulador**:

```bash
./gradlew testDebugUnitTest
```

Cubren: análisis de movimiento y rotación con datos simulados, FFT con
señales sintéticas, análisis de amplitud/frecuencia de audio sintético,
sensor inexistente, persistencia en Room, otorgamiento de insignias, y
manejo de permiso de micrófono denegado.

## 🏗️ Compilación local

```bash
./gradlew assembleDebug
```

> Nota: si es la primera vez que clonas el repositorio y `gradlew` falla
> por falta de `gradle-wrapper.jar`, ejecuta `gradle wrapper` con una
> instalación local de Gradle 8.9, o simplemente deja que el workflow de
> GitHub Actions compile el proyecto por ti (ver abajo).

## 🚀 Publicar este proyecto en GitHub

Este proyecto se generó como código fuente completo, pero **no se ha
compilado, publicado ni subido a GitHub automáticamente** (el entorno de
generación no tiene acceso a Internet ni SDK de Android). Sigue la guía
[`SUBIR_A_GITHUB.md`](SUBIR_A_GITHUB.md) para subirlo tú mismo y dejar que
los workflows de `.github/workflows/` hagan el resto.

## ⚠️ Aviso científico

> Los sensores de un teléfono sirven para aprender y comparar, pero no
> sustituyen instrumentos científicos calibrados.

## 🔒 Seguridad física en cada experimento

Todos los experimentos se realizan **sentado, caminando despacio, o con el
dispositivo apoyado sobre una superficie segura**. Ningún experimento de
esta app requiere lanzar, dejar caer, mojar, acercar al fuego, conectar a
electricidad, usar en una bicicleta en movimiento, llevar a una carretera,
subir a una altura, ni golpear el dispositivo.
