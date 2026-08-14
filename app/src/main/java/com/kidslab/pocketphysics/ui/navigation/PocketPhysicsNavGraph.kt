package com.kidslab.pocketphysics.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.di.AppContainer
import com.kidslab.pocketphysics.ui.screens.challenges.ChallengesScreen
import com.kidslab.pocketphysics.ui.screens.experiments.ExperimentsScreen
import com.kidslab.pocketphysics.ui.screens.home.HomeScreen
import com.kidslab.pocketphysics.ui.screens.light.LightLabScreen
import com.kidslab.pocketphysics.ui.screens.log.LogScreen
import com.kidslab.pocketphysics.ui.screens.motion.MotionLabScreen
import com.kidslab.pocketphysics.ui.screens.profile.ProfileScreen
import com.kidslab.pocketphysics.ui.screens.rotation.RotationLabScreen
import com.kidslab.pocketphysics.ui.screens.sensors.SensorsScreen
import com.kidslab.pocketphysics.ui.screens.sound.SoundLabScreen

/** Rutas de navegación de Física de Bolsillo. */
object Rutas {
    const val PERFIL = "perfil"
    const val INICIO = "inicio"
    const val SENSORES = "sensores"
    const val LAB_MOVIMIENTO = "lab_movimiento"
    const val LAB_ROTACION = "lab_rotacion"
    const val LAB_SONIDO = "lab_sonido"
    const val LAB_LUZ = "lab_luz"
    const val EXPERIMENTOS = "experimentos"
    const val REGISTRO = "registro"
    const val DESAFIOS = "desafios"
}

private data class ItemBarraNav(val ruta: String, val labelRes: Int, val icono: androidx.compose.ui.graphics.vector.ImageVector)

private val itemsBarraInferior = listOf(
    ItemBarraNav(Rutas.INICIO, R.string.nav_inicio, Icons.Filled.Home),
    ItemBarraNav(Rutas.SENSORES, R.string.nav_sensores, Icons.Filled.Sensors),
    ItemBarraNav(Rutas.EXPERIMENTOS, R.string.nav_experimentos, Icons.Filled.Science),
    ItemBarraNav(Rutas.REGISTRO, R.string.nav_registro, Icons.Filled.Book),
    ItemBarraNav(Rutas.DESAFIOS, R.string.nav_desafios, Icons.Filled.EmojiEvents)
)

/**
 * Grafo de navegación principal. Usa [rememberNavController] (memoizado con
 * `remember` internamente) para evitar que el NavHost se reconstruya en cada
 * recomposición, tal como se aprendió en HabitHero.
 */
@Composable
fun PocketPhysicsNavGraph(container: AppContainer) {
    val navController: NavHostController = rememberNavController()
    val factory = remember(container) { com.kidslab.pocketphysics.ui.AppViewModelFactory(container) }

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination
            NavigationBar {
                itemsBarraInferior.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute?.hierarchy?.any { it.route == item.ruta } == true,
                        onClick = {
                            navController.navigate(item.ruta) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icono, contentDescription = stringResource(item.labelRes)) },
                        label = { Text(stringResource(item.labelRes)) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.PERFIL,
            modifier = androidx.compose.ui.Modifier.padding(padding)
        ) {
            composable(Rutas.PERFIL) {
                ProfileScreen(
                    factory = factory,
                    onPerfilListo = {
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.PERFIL) { inclusive = true }
                        }
                    }
                )
            }
            composable(Rutas.INICIO) {
                HomeScreen(navController = navController)
            }
            composable(Rutas.SENSORES) {
                SensorsScreen(factory = factory)
            }
            composable(Rutas.LAB_MOVIMIENTO) {
                MotionLabScreen(factory = factory)
            }
            composable(Rutas.LAB_ROTACION) {
                RotationLabScreen(factory = factory)
            }
            composable(Rutas.LAB_SONIDO) {
                SoundLabScreen(factory = factory)
            }
            composable(Rutas.LAB_LUZ) {
                LightLabScreen(factory = factory)
            }
            composable(Rutas.EXPERIMENTOS) {
                ExperimentsScreen(factory = factory, navController = navController)
            }
            composable(Rutas.REGISTRO) {
                LogScreen(factory = factory)
            }
            composable(Rutas.DESAFIOS) {
                ChallengesScreen(factory = factory)
            }
        }
    }
}
