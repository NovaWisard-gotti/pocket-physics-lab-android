package com.kidslab.pocketphysics.ui.screens.motion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidslab.pocketphysics.data.repository.ChallengeRepository
import com.kidslab.pocketphysics.data.repository.ExperimentRepository
import com.kidslab.pocketphysics.data.repository.ProfileRepository
import com.kidslab.pocketphysics.data.repository.SensorRepository
import com.kidslab.pocketphysics.domain.model.MotionSample
import com.kidslab.pocketphysics.domain.model.SensorType
import com.kidslab.pocketphysics.domain.usecase.MotionAnalysisUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val MAX_MUESTRAS_GRAFICO = 120

data class MotionLabUiState(
    val disponible: Boolean = true,
    val midiendo: Boolean = false,
    val muestraActual: MotionSample? = null,
    val historialMagnitud: List<Float> = emptyList(),
    val catalogo: List<com.kidslab.pocketphysics.data.local.entity.SensorExperiment> = emptyList(),
    val experimentoKey: String? = null,
    val prediccion: String = "",
    val resultadoTexto: String? = null,
    val insigniaGanada: Boolean = false
)

class MotionLabViewModel(
    private val sensorRepository: SensorRepository,
    private val profileRepository: ProfileRepository,
    private val experimentRepository: ExperimentRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MotionLabUiState(disponible = sensorRepository.isSensorAvailable(SensorType.ACCELEROMETER))
    )
    val uiState: StateFlow<MotionLabUiState> = _uiState.asStateFlow()

    private var medicionJob: Job? = null
    private val muestrasSesion = mutableListOf<MotionSample>()

    init {
        viewModelScope.launch {
            experimentRepository.observeCatalogByLab("movimiento").collect { lista ->
                _uiState.value = _uiState.value.copy(
                    catalogo = lista,
                    experimentoKey = _uiState.value.experimentoKey ?: lista.firstOrNull()?.experimentKey
                )
            }
        }
    }

    fun seleccionarExperimento(experimentKey: String) {
        _uiState.value = _uiState.value.copy(experimentoKey = experimentKey, resultadoTexto = null, insigniaGanada = false)
    }

    fun onPrediccionChanged(texto: String) {
        _uiState.value = _uiState.value.copy(prediccion = texto)
    }

    fun iniciarMedicion() {
        if (!_uiState.value.disponible || medicionJob != null) return
        muestrasSesion.clear()
        _uiState.value = _uiState.value.copy(midiendo = true, historialMagnitud = emptyList(), resultadoTexto = null)
        medicionJob = viewModelScope.launch {
            sensorRepository.observeAccelerometer().collect { sample ->
                muestrasSesion.add(sample)
                val nuevoHistorial = (_uiState.value.historialMagnitud + sample.magnitude).takeLast(MAX_MUESTRAS_GRAFICO)
                _uiState.value = _uiState.value.copy(muestraActual = sample, historialMagnitud = nuevoHistorial)
            }
        }
    }

    fun detenerMedicionYGuardar() {
        medicionJob?.cancel()
        medicionJob = null
        _uiState.value = _uiState.value.copy(midiendo = false)

        val experimentKey = _uiState.value.experimentoKey ?: return
        val muestras = muestrasSesion.toList()
        if (muestras.isEmpty()) return

        viewModelScope.launch {
            val profileId = profileRepository.observeProfile().first()?.id ?: return@launch

            val summary = MotionAnalysisUseCase.summarize(muestras)
            val estuvoQuieto = MotionAnalysisUseCase.estuvoQuieto(muestras)
            val prediccionTexto = _uiState.value.prediccion.ifBlank { "(sin predicción escrita)" }

            val textoResultado = if (estuvoQuieto) {
                "El teléfono se mantuvo prácticamente quieto: la magnitud varió muy poco alrededor de la gravedad."
            } else {
                "El teléfono se movió: la magnitud pasó de ${"%.2f".format(summary.minimo)} a ${"%.2f".format(summary.maximo)} m/s²."
            }
            val conclusion = "Promedio de la sesión: ${"%.2f".format(summary.promedio)} m/s² en ${summary.muestras} muestras."

            experimentRepository.saveSession(
                profileId = profileId,
                experimentKey = experimentKey,
                sensorType = SensorType.ACCELEROMETER.name,
                predictionText = prediccionTexto,
                summary = summary,
                prediccionCorrecta = true, // el niño compara su predicción con el texto, no hay "falso/verdadero" estricto
                textoResultado = textoResultado,
                textoConclusion = conclusion
            )

            var insigniaGanada = false
            if (estuvoQuieto) {
                insigniaGanada = challengeRepository.grantBadgeIfNew(profileId, "badge_explorador_quieto")
            }

            _uiState.value = _uiState.value.copy(resultadoTexto = "$textoResultado $conclusion", insigniaGanada = insigniaGanada)
        }
    }

    override fun onCleared() {
        super.onCleared()
        medicionJob?.cancel()
    }
}
