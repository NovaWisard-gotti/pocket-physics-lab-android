package com.kidslab.pocketphysics.ui.screens.light

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.ui.AppViewModelFactory
import com.kidslab.pocketphysics.ui.components.ExperimentSelector
import com.kidslab.pocketphysics.ui.components.FunButton
import com.kidslab.pocketphysics.ui.components.FunOutlinedButton
import com.kidslab.pocketphysics.ui.components.LiveMeasuringBadge
import com.kidslab.pocketphysics.ui.components.PreguntaYPrediccion
import com.kidslab.pocketphysics.ui.components.ResultadoYConclusion
import com.kidslab.pocketphysics.ui.components.SimpleLineChart
import com.kidslab.pocketphysics.ui.theme.AppBackgroundGradient
import com.kidslab.pocketphysics.ui.theme.LabGradient

@Composable
fun LightLabScreen(factory: AppViewModelFactory) {
    val viewModel: LightLabViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()
    val gradiente = LabGradient.LUZ

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(AppBackgroundGradient))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                gradiente.emoji + " " + stringResource(R.string.lab_luz_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                stringResource(R.string.lab_luz_instruccion),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            if (!state.disponible) {
                Text(stringResource(R.string.sensor_no_disponible), color = MaterialTheme.colorScheme.error)
                Text(
                    "Experimento alternativo: usa el Laboratorio de sonido o de movimiento mientras exploras luz y sombra a simple vista.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
                return@Column
            }

            ExperimentSelector(state.catalogo, state.experimentoKey, viewModel::seleccionarExperimento)

            val experimentoActual = state.catalogo.find { it.experimentKey == state.experimentoKey }
            if (experimentoActual != null) {
                PreguntaYPrediccion(
                    pregunta = experimentoActual.pregunta,
                    prediccion = state.prediccion,
                    onPrediccionChanged = viewModel::onPrediccionChanged,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FunButton(
                    text = "Medir luz",
                    emoji = "💡",
                    onClick = viewModel::iniciarMedicion,
                    enabled = !state.midiendo,
                    gradient = gradiente.colors,
                    modifier = Modifier.weight(1f)
                )
                FunOutlinedButton(
                    text = "Detener",
                    emoji = "⏹️",
                    onClick = viewModel::detenerMedicionYGuardar,
                    enabled = state.midiendo,
                    color = gradiente.colors.first(),
                    modifier = Modifier.weight(1f)
                )
            }

            if (state.midiendo) {
                LiveMeasuringBadge(texto = "Midiendo la luz…", color = gradiente.colors.first(), modifier = Modifier.padding(top = 12.dp))
            }

            Text(
                "☀️ Lux actual: ${"%.0f".format(state.luxActual)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )

            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FunOutlinedButton(
                    text = "Habitación",
                    emoji = "🏠",
                    onClick = viewModel::registrarComoHabitacion,
                    enabled = state.midiendo,
                    color = gradiente.colors.first(),
                    modifier = Modifier.weight(1f)
                )
                FunOutlinedButton(
                    text = "Sombra",
                    emoji = "🌑",
                    onClick = viewModel::registrarComoSombra,
                    enabled = state.midiendo,
                    color = gradiente.colors[1],
                    modifier = Modifier.weight(1f)
                )
            }

            if (state.historialLux.isNotEmpty()) {
                Text("📈 Lux en el tiempo", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 12.dp))
                SimpleLineChart(values = state.historialLux, lineColor = gradiente.colors.first(), modifier = Modifier.padding(top = 4.dp))
            }

            state.resultadoTexto?.let { texto ->
                ResultadoYConclusion(texto = texto, insigniaGanada = state.insigniaGanada, modifier = Modifier.padding(top = 16.dp))
            }

            Text(
                text = stringResource(R.string.aviso_precision),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}
