package com.kidslab.pocketphysics.ui.screens.rotation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.ui.AppViewModelFactory
import com.kidslab.pocketphysics.ui.components.ExperimentSelector
import com.kidslab.pocketphysics.ui.components.PreguntaYPrediccion
import com.kidslab.pocketphysics.ui.components.ResultadoYConclusion
import com.kidslab.pocketphysics.ui.components.SimpleLineChart

@Composable
fun RotationLabScreen(factory: AppViewModelFactory) {
    val viewModel: RotationLabViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(stringResource(R.string.lab_rotacion_titulo), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.lab_rotacion_instruccion), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))

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

        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = viewModel::iniciarMedicion, enabled = !state.midiendo, modifier = Modifier.weight(1f)) {
                Text("Empezar a girar")
            }
            OutlinedButton(onClick = viewModel::detenerMedicionYGuardar, enabled = state.midiendo, modifier = Modifier.weight(1f)) {
                Text("Detener y guardar")
            }
        }

        Text(
            "Ángulo estimado: ${"%.0f".format(state.anguloEstimado)}°",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )

        if (state.historialMagnitud.isNotEmpty()) {
            Text("Velocidad de giro en el tiempo", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 12.dp))
            SimpleLineChart(values = state.historialMagnitud, modifier = Modifier.padding(top = 4.dp))
        }

        state.resultadoTexto?.let { texto ->
            ResultadoYConclusion(texto = texto, insigniaGanada = state.insigniaGanada, modifier = Modifier.padding(top = 16.dp))
        }

        Text(
            text = stringResource(R.string.aviso_precision),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}
