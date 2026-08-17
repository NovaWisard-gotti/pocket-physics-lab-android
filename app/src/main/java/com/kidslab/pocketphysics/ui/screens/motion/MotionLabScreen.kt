package com.kidslab.pocketphysics.ui.screens.motion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun MotionLabScreen(factory: AppViewModelFactory) {
    val viewModel: MotionLabViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()
    val gradiente = LabGradient.MOVIMIENTO

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(AppBackgroundGradient))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                gradiente.emoji + " " + stringResource(R.string.lab_movimiento_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                stringResource(R.string.lab_movimiento_instruccion),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            if (!state.disponible) {
                Text(stringResource(R.string.sensor_no_disponible), color = MaterialTheme.colorScheme.error)
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

            if (state.resultadoTexto == null) {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FunButton(
                        text = "Empezar a medir",
                        emoji = "▶️",
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
            } else {
                FunButton(
                    text = "Reiniciar prueba",
                    emoji = "🔄",
                    onClick = viewModel::reiniciarPrueba,
                    gradient = gradiente.colors,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            if (state.midiendo) {
                LiveMeasuringBadge(texto = "Midiendo movimiento…", color = gradiente.colors.first(), modifier = Modifier.padding(top = 12.dp))
            }

            state.muestraActual?.let { muestra ->
                Text(
                    "X: ${"%.2f".format(muestra.x)}   Y: ${"%.2f".format(muestra.y)}   Z: ${"%.2f".format(muestra.z)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            if (state.historialMagnitud.isNotEmpty()) {
                Text("📈 Magnitud del movimiento en el tiempo", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 12.dp))
                SimpleLineChart(
                    values = state.historialMagnitud,
                    lineColor = gradiente.colors.first(),
                    modifier = Modifier.padding(top = 4.dp)
                )
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
