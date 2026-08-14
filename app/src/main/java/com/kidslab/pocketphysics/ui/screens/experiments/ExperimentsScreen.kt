package com.kidslab.pocketphysics.ui.screens.experiments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.data.local.entity.SensorExperiment
import com.kidslab.pocketphysics.ui.AppViewModelFactory
import com.kidslab.pocketphysics.ui.navigation.Rutas

private fun rutaParaLab(labKey: String): String = when (labKey) {
    "movimiento" -> Rutas.LAB_MOVIMIENTO
    "rotacion" -> Rutas.LAB_ROTACION
    "sonido" -> Rutas.LAB_SONIDO
    "luz" -> Rutas.LAB_LUZ
    else -> Rutas.INICIO
}

private fun nombreLab(labKey: String): String = when (labKey) {
    "movimiento" -> "Movimiento"
    "rotacion" -> "Rotación"
    "sonido" -> "Sonido"
    "luz" -> "Luz"
    else -> labKey
}

@Composable
fun ExperimentsScreen(factory: AppViewModelFactory, navController: NavHostController) {
    val viewModel: ExperimentsViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(stringResource(R.string.experimentos_titulo), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(
            "${state.experimentos.size} experimentos guiados disponibles",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.experimentos) { experimento ->
                ExperimentoCard(experimento) { navController.navigate(rutaParaLab(experimento.labKey)) }
            }
        }
    }
}

@Composable
private fun ExperimentoCard(experimento: SensorExperiment, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Laboratorio de ${nombreLab(experimento.labKey)}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(experimento.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(experimento.pregunta, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
        }
    }
}
