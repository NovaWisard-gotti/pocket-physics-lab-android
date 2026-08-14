package com.kidslab.pocketphysics.ui.screens.sensors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidslab.pocketphysics.data.repository.SensorRepository
import com.kidslab.pocketphysics.data.repository.SoundRepository
import com.kidslab.pocketphysics.domain.model.SensorAvailability
import com.kidslab.pocketphysics.domain.model.SensorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SensorsUiState(
    val sensores: List<SensorAvailability> = emptyList(),
    val cargando: Boolean = true
)

/**
 * Construye la lista de disponibilidad de sensores mostrada en "Mis sensores".
 * Es una función pura (fuera del ViewModel) para poder probarla sin Android.
 */
fun buildSensorAvailability(
    hasAccelerometer: Boolean,
    hasGyroscope: Boolean,
    hasMicrophone: Boolean,
    hasLight: Boolean
): List<SensorAvailability> = listOf(
    SensorAvailability(
        SensorType.ACCELEROMETER, hasAccelerometer,
        "Mide qué tan rápido cambia el movimiento del teléfono en tres direcciones."
    ),
    SensorAvailability(
        SensorType.GYROSCOPE, hasGyroscope,
        "Mide qué tan rápido gira el teléfono sobre sí mismo."
    ),
    SensorAvailability(
        SensorType.MICROPHONE, hasMicrophone,
        "Capta el sonido del ambiente para estudiar su intensidad y su tono."
    ),
    SensorAvailability(
        SensorType.LIGHT, hasLight,
        "Mide cuánta luz llega a la pantalla del teléfono."
    )
)

class SensorsViewModel(
    private val sensorRepository: SensorRepository,
    private val soundRepository: SoundRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SensorsUiState())
    val uiState: StateFlow<SensorsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val sensores = buildSensorAvailability(
                hasAccelerometer = sensorRepository.isSensorAvailable(SensorType.ACCELEROMETER),
                hasGyroscope = sensorRepository.isSensorAvailable(SensorType.GYROSCOPE),
                hasMicrophone = soundRepository.isMicrophoneAvailable(),
                hasLight = sensorRepository.isSensorAvailable(SensorType.LIGHT)
            )
            _uiState.value = SensorsUiState(sensores = sensores, cargando = false)
        }
    }
}
