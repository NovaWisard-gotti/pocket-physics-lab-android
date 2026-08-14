package com.kidslab.pocketphysics.ui.screens.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.data.local.entity.ExperimentSessionFull
import com.kidslab.pocketphysics.ui.AppViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val FORMATO_FECHA = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "PE"))

@Composable
fun LogScreen(factory: AppViewModelFactory) {
    val viewModel: LogViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(stringResource(R.string.registro_titulo), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(
            "Cada fila guarda un resumen: nunca las muestras crudas del sensor.",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        if (state.sesiones.isEmpty() && !state.cargando) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 32.dp), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.registro_vacio), style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.sesiones) { sesion -> SesionCard(sesion) }
            }
        }
    }
}

@Composable
private fun SesionCard(sesion: ExperimentSessionFull) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                sesion.experiment?.titulo ?: sesion.session.experimentKey,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(FORMATO_FECHA.format(Date(sesion.session.fecha)), style = MaterialTheme.typography.labelLarge)

            sesion.prediction?.let {
                Text("Predicción: ${it.textoPrediccion}", modifier = Modifier.padding(top = 8.dp))
            }
            sesion.summary?.let {
                Text(
                    "Mín: ${"%.1f".format(it.valorMinimo)}  Máx: ${"%.1f".format(it.valorMaximo)}  Prom: ${"%.1f".format(it.valorPromedio)}",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            sesion.result?.let {
                Text("Resultado: ${it.textoResultado}", modifier = Modifier.padding(top = 8.dp))
                Text("Conclusión: ${it.textoConclusion}", modifier = Modifier.padding(top = 4.dp), fontWeight = FontWeight.Medium)
            }
        }
    }
}
