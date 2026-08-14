# Cómo subir Física de Bolsillo a GitHub

Esta guía asume que **no tienes Git instalado** y que vas a hacerlo todo
desde el navegador (funciona igual si prefieres usar Git desde la terminal;
al final se incluye esa alternativa).

## Opción A — Solo con el navegador (recomendada si no usas Git)

### 1. Crea el repositorio vacío

1. Entra a [github.com](https://github.com) y accede a tu cuenta.
2. Toca el botón **"New repository"** (Nuevo repositorio).
3. Nombre del repositorio: `pocket-physics-lab-android`
4. Descripción sugerida: *"Física de Bolsillo — laboratorio de física de bolsillo para niños de 8 a 12 años"*
5. Visibilidad: pública o privada, como prefieras.
6. **No** marques "Add a README file" (ya incluimos uno en el ZIP).
7. Toca **"Create repository"**.

### 2. Sube los archivos

1. En la página del repositorio recién creado, busca el enlace
   **"uploading an existing file"** (o el botón **"Add file" → "Upload
   files"**).
2. Descomprime el ZIP que te entregó Claude en tu computadora.
3. Arrastra **todo el contenido de la carpeta** (no la carpeta en sí, sino
   lo que está dentro: `app/`, `database/`, `docs/`, `gradle/`, `.github/`,
   `settings.gradle.kts`, etc.) a la zona de subida de GitHub.
   - Si GitHub te limita la cantidad de archivos por subida, hazlo en
     varias tandas: primero `app/`, luego `docs/` y `database/`, luego el
     resto de archivos sueltos y `.github/`.
4. Escribe un mensaje de commit, por ejemplo: `Primera versión de Física de Bolsillo`.
5. Toca **"Commit changes"**.

### 3. Verifica que los workflows se activen

1. Ve a la pestaña **"Actions"** de tu repositorio.
2. Deberías ver el workflow **"Android Build"** ejecutándose automáticamente
   (se dispara con cada `push` a `main`).
3. Espera a que termine. Si aparece en rojo (❌), toca el workflow para ver
   el error y compártelo aquí para corregirlo juntos.
4. Cuando aparezca en verde (✅), el APK de depuración estará disponible
   como artefacto descargable en esa misma ejecución.

### 4. Publica el Release v1.0.0

1. Ve a la pestaña **"Actions" → "Release"**.
2. Toca **"Run workflow"**, elige la rama `main`, y en el campo de versión
   escribe `v1.0.0`.
3. Ejecuta el workflow y espera a que termine en verde.
4. Ve a la pestaña **"Releases"** de tu repositorio: deberías ver
   **"Física de Bolsillo v1.0.0"** con el archivo
   `FisicaDeBolsillo-v1.0.0.apk` y los tres PDFs adjuntos.

## Opción B — Con Git desde la terminal

Si tienes Git instalado en tu computadora:

```bash
cd pocket-physics-lab-android
git init
git add .
git commit -m "Primera versión de Física de Bolsillo"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/pocket-physics-lab-android.git
git push -u origin main

# Para disparar el workflow de Release:
git tag v1.0.0
git push origin v1.0.0
```

## Si algo falla en GitHub Actions

Copia el error completo de la pestaña **"Actions"** (el log rojo) y
compártelo en tu próxima conversación con Claude para corregirlo. Es
normal necesitar una o dos rondas de ajustes la primera vez que un
proyecto nuevo se compila en un entorno limpio de CI.

## ¿Por qué no lo subió Claude directamente?

El entorno donde Claude generó este proyecto **no tiene acceso a
Internet, ni Git, ni un conector de GitHub configurado**. Por eso el
proyecto se entrega como código fuente completo en un ZIP, listo para que
tú lo subas siguiendo esta guía, y para que los workflows de
`.github/workflows/` hagan la compilación real en la infraestructura de
GitHub.
