package com.kidslab.pocketphysics.ui.screens.sound

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.ui.AppViewModelFactory
import com.kidslab.pocketphysics.ui.components.ExperimentSelector
import com.kidslab.pocketphysics.ui.components.PreguntaYPrediccion
import com.kidslab.pocketphysics.ui.components.ResultadoYConclusion
import com.kidslab.pocketphysics.ui.components.SimpleLineChart

@Composable
fun SoundLabScreen(factory: AppViewModelFactory) {
    val viewModel: SoundLabViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val permisoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        viewModel.onPermisoResultado(concedido)
    }

    LaunchedEffect(Unit) {
        val yaConcedido = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        viewModel.onPermisoResultado(yaConcedido)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(stringResource(R.string.lab_sonido_titulo), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        if (!state.disponible) {
            Text(stringResource(R.string.sensor_no_disponible), color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            return@Column
        }

        if (!state.permisoConcedido) {
            Text(stringResource(R.string.lab_sonido_permiso_explicacion), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
            Button(onClick = { permisoLauncher.launch(Manifest.permission.RECORD_AUDIO) }) {
                Text(stringResource(R.string.lab_sonido_permiso_boton))
            }
            if (state.permisoFueDenegado) {
                Text(
                    stringResource(R.string.lab_sonido_permiso_denegado),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
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
            Button(onClick = viewModel::iniciarEscucha, enabled = !state.escuchando, modifier = Modifier.weight(1f)) {
                Text("Escuchar")
            }
            OutlinedButton(onClick = viewModel::detenerEscuchaYGuardar, enabled = state.escuchando, modifier = Modifier.weight(1f)) {
                Text("Detener y guardar")
            }
        }

        state.ultimoAnalisis?.let { analisis ->
            Text(
                "Amplitud: ${"%.0f".format(analisis.amplitudPromedio)}   Frecuencia aprox: ${"%.0f".format(analisis.frecuenciaDominanteHz)} Hz",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (state.historialAmplitud.isNotEmpty()) {
            Text("Amplitud del sonido en el tiempo", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 12.dp))
            SimpleLineChart(values = state.historialAmplitud, modifier = Modifier.padding(top = 4.dp))
        }

        state.ultimoAnalisis?.forma?.let { forma ->
            Text("Forma de onda simplificada", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 12.dp))
            SimpleLineChart(values = forma, minValue = -1f, maxValue = 1f, modifier = Modifier.padding(top = 4.dp))
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
