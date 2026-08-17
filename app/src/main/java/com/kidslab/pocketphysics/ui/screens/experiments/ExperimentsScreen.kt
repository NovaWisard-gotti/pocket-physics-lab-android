package com.kidslab.pocketphysics.ui.screens.experiments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.data.local.entity.SensorExperiment
import com.kidslab.pocketphysics.ui.AppViewModelFactory
import com.kidslab.pocketphysics.ui.components.BouncyCard
import com.kidslab.pocketphysics.ui.components.VerticalScrollHint
import com.kidslab.pocketphysics.ui.navigation.Rutas
import com.kidslab.pocketphysics.ui.theme.AppBackgroundGradient
import com.kidslab.pocketphysics.ui.theme.LabGradient

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
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(AppBackgroundGradient))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Text(
                text = "🧪 " + stringResource(R.string.experimentos_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                "${state.experimentos.size} experimentos guiados disponibles",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.experimentos) { experimento ->
                    ExperimentoCard(experimento) { navController.navigate(rutaParaLab(experimento.labKey)) }
                }
            }
            VerticalScrollHint(listState = listState, texto = "Hay más experimentos abajo ↓", modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun ExperimentoCard(experimento: SensorExperiment, onClick: () -> Unit) {
    val gradiente = LabGradient.forLabKey(experimento.labKey)
    BouncyCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).background(Brush.linearGradient(gradiente.colors), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(gradiente.emoji, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Laboratorio de ${nombreLab(experimento.labKey)}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(experimento.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    experimento.pregunta,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
