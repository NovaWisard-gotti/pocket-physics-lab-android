package com.kidslab.pocketphysics.ui.screens.sensors

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.domain.model.SensorAvailability
import com.kidslab.pocketphysics.domain.model.SensorType
import com.kidslab.pocketphysics.ui.AppViewModelFactory
import com.kidslab.pocketphysics.ui.theme.AppBackgroundGradient
import com.kidslab.pocketphysics.ui.theme.LabGradient
import com.kidslab.pocketphysics.ui.theme.PhysicsGreen
import com.kidslab.pocketphysics.ui.theme.PhysicsRedSoft

private fun nombreSensor(type: SensorType): Int = when (type) {
    SensorType.ACCELEROMETER -> R.string.sensor_acelerometro_nombre
    SensorType.GYROSCOPE -> R.string.sensor_giroscopio_nombre
    SensorType.MICROPHONE -> R.string.sensor_microfono_nombre
    SensorType.LIGHT -> R.string.sensor_luz_nombre
}

private fun gradienteSensor(type: SensorType): LabGradient = when (type) {
    SensorType.ACCELEROMETER -> LabGradient.MOVIMIENTO
    SensorType.GYROSCOPE -> LabGradient.ROTACION
    SensorType.MICROPHONE -> LabGradient.SONIDO
    SensorType.LIGHT -> LabGradient.LUZ
}

@Composable
fun SensorsScreen(factory: AppViewModelFactory) {
    val viewModel: SensorsViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(AppBackgroundGradient))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Text(
                text = "📡 " + stringResource(R.string.pantalla_sensores_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = stringResource(R.string.aviso_precision),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                items(state.sensores) { sensor ->
                    SensorCard(sensor)
                }
            }
        }
    }
}

@Composable
private fun SensorCard(sensor: SensorAvailability) {
    val gradiente = gradienteSensor(sensor.type)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Brush.linearGradient(gradiente.colors), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(gradiente.emoji, style = MaterialTheme.typography.titleLarge)
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(nombreSensor(sensor.type)),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                EstadoBadge(disponible = sensor.isAvailable)
            }
            Text(
                text = sensor.explicacionCorta,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 10.dp)
            )
            if (!sensor.isAvailable) {
                Text(
                    text = stringResource(R.string.sensor_no_disponible),
                    color = PhysicsRedSoft,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "${stringResource(R.string.sensor_alternativa_titulo)}: prueba otro laboratorio que sí use un sensor disponible en tu dispositivo.",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun EstadoBadge(disponible: Boolean) {
    val color = if (disponible) PhysicsGreen else PhysicsRedSoft
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.18f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = if (disponible) "✅ " + stringResource(R.string.sensor_disponible) else "🚫 No disponible",
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
