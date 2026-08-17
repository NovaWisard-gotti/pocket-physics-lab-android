package com.kidslab.pocketphysics.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kidslab.pocketphysics.data.local.entity.SensorExperiment
import com.kidslab.pocketphysics.ui.theme.LabGradient
import com.kidslab.pocketphysics.ui.theme.PhysicsGreen

/** Selector horizontal de experimentos guiados de un laboratorio, con pista de scroll. */
@Composable
fun ExperimentSelector(
    experimentos: List<SensorExperiment>,
    seleccionado: String?,
    onSeleccionar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    Column(modifier = modifier.fillMaxWidth()) {
        LazyRow(state = listState, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(experimentos) { experimento ->
                val gradiente = LabGradient.forLabKey(experimento.labKey)
                val seleccionadoActual = experimento.experimentKey == seleccionado
                BouncyCard(
                    onClick = { onSeleccionar(experimento.experimentKey) },
                    shape = RoundedCornerShape(50),
                    backgroundBrush = if (seleccionadoActual) Brush.horizontalGradient(gradiente.colors) else null,
                    backgroundColor = if (seleccionadoActual) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(gradiente.emoji, modifier = Modifier.padding(end = 6.dp))
                        Text(
                            experimento.titulo,
                            color = if (seleccionadoActual) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (seleccionadoActual) FontWeight.Bold else FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        HorizontalScrollHint(listState = listState, texto = "Más pruebas para elegir →")
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
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("🤔 Pregunta", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(pregunta, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
            Text(
                "✍️ Tu predicción",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 14.dp)
            )
            OutlinedTextField(
                value = prediccion,
                onValueChange = onPrediccionChanged,
                placeholder = { Text("¿Qué crees que va a pasar?") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            )
        }
    }
}

/** Bloque de Resultado + Conclusión, mostrado tras medir. Celebra si se ganó una insignia. */
@Composable
fun ResultadoYConclusion(texto: String, insigniaGanada: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("📋 Resultado y conclusión", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text(texto, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 6.dp))
            if (insigniaGanada) {
                CelebracionInsignia()
            }
        }
    }
}

@Composable
private fun CelebracionInsignia() {
    val infiniteTransition = rememberInfiniteTransition(label = "celebracionInsignia")
    val escala by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(animation = tween(500), repeatMode = RepeatMode.Reverse),
        label = "celebracionEscala"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 10.dp)
            .background(PhysicsGreen.copy(alpha = 0.18f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text("🎉", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.scale(escala))
        Text(
            "¡Ganaste una insignia nueva! Revisa Desafíos y logros. 🏅",
            color = PhysicsGreen,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
