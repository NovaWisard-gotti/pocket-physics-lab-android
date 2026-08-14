package com.kidslab.pocketphysics

import android.app.Application
import com.kidslab.pocketphysics.di.AppContainer

/**
 * Aplicación principal de Física de Bolsillo.
 *
 * Contiene el [AppContainer], un contenedor de inyección de dependencias manual
 * (sin Hilt/Dagger) que expone la base de datos y los repositorios a todo el app.
 * No se usa Internet en ningún punto de la aplicación.
 */
class PocketPhysicsApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
