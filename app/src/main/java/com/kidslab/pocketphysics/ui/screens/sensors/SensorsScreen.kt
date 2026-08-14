package com.kidslab.pocketphysics.ui.screens.sensors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.domain.model.SensorAvailability
import com.kidslab.pocketphysics.domain.model.SensorType
import com.kidslab.pocketphysics.ui.AppViewModelFactory
import com.kidslab.pocketphysics.ui.theme.PhysicsGreen
import com.kidslab.pocketphysics.ui.theme.PhysicsRedSoft

private fun nombreSensor(type: SensorType): Int = when (type) {
    SensorType.ACCELEROMETER -> R.string.sensor_acelerometro_nombre
    SensorType.GYROSCOPE -> R.string.sensor_giroscopio_nombre
    SensorType.MICROPHONE -> R.string.sensor_microfono_nombre
    SensorType.LIGHT -> R.string.sensor_luz_nombre
}

@Composable
fun SensorsScreen(factory: AppViewModelFactory) {
    val viewModel: SensorsViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(
            text = stringResource(R.string.pantalla_sensores_titulo),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.aviso_precision),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(state.sensores) { sensor ->
                SensorCard(sensor)
            }
        }
    }
}

@Composable
private fun SensorCard(sensor: SensorAvailability) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(nombreSensor(sensor.type)),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (sensor.isAvailable) stringResource(R.string.sensor_disponible) else "No disponible",
                    color = if (sensor.isAvailable) PhysicsGreen else PhysicsRedSoft,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = sensor.explicacionCorta,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 6.dp)
            )
            if (!sensor.isAvailable) {
                Text(
                    text = stringResource(R.string.sensor_no_disponible),
                    color = PhysicsRedSoft,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Text(
                    text = "${stringResource(R.string.sensor_alternativa_titulo)}: prueba otro laboratorio que sí use un sensor disponible en tu dispositivo.",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
