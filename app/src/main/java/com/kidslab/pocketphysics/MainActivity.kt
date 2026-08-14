package com.kidslab.pocketphysics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kidslab.pocketphysics.ui.navigation.PocketPhysicsNavGraph
import com.kidslab.pocketphysics.ui.theme.PocketPhysicsTheme

/**
 * Actividad única de Física de Bolsillo. Toda la navegación ocurre dentro
 * de Compose (ver [PocketPhysicsNavGraph]). No hay Internet en ningún punto.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as PocketPhysicsApplication).container

        setContent {
            PocketPhysicsApp(container)
        }
    }
}

@Composable
private fun PocketPhysicsApp(container: com.kidslab.pocketphysics.di.AppContainer) {
    PocketPhysicsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            PocketPhysicsNavGraph(container = container)
        }
    }
}
