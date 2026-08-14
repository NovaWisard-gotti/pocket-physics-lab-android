package com.kidslab.pocketphysics.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kidslab.pocketphysics.data.local.entity.SensorExperiment
import com.kidslab.pocketphysics.ui.theme.PhysicsGreen

/** Selector horizontal de experimentos guiados de un laboratorio. */
@Composable
fun ExperimentSelector(
    experimentos: List<SensorExperiment>,
    seleccionado: String?,
    onSeleccionar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(experimentos) { experimento ->
            FilterChip(
                selected = experimento.experimentKey == seleccionado,
                onClick = { onSeleccionar(experimento.experimentKey) },
                label = { Text(experimento.titulo) }
            )
        }
    }
}

/** Bloque de Pregunta + Predicción (input) para el experimento activo. */
@Composable
fun PreguntaYPrediccion(
    pregunta: String,
    prediccion: String,
    onPrediccionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pregunta", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text(pregunta, style = MaterialTheme.typography.bodyLarge)
            Text("Predicción", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
            OutlinedTextField(
                value = prediccion,
                onValueChange = onPrediccionChanged,
                placeholder = { Text("¿Qué crees que va a pasar?") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/** Bloque de Resultado + Conclusión, mostrado tras medir. */
@Composable
fun ResultadoYConclusion(texto: String, insigniaGanada: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Resultado y conclusión", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text(texto, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
            if (insigniaGanada) {
                Text(
                    "🏅 ¡Ganaste una insignia nueva! Revisa Desafíos y logros.",
                    color = PhysicsGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
