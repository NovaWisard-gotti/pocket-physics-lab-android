package com.kidslab.pocketphysics.ui.screens.rotation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidslab.pocketphysics.data.repository.ChallengeRepository
import com.kidslab.pocketphysics.data.repository.ExperimentRepository
import com.kidslab.pocketphysics.data.repository.ProfileRepository
import com.kidslab.pocketphysics.data.repository.SensorRepository
import com.kidslab.pocketphysics.domain.model.RotationSample
import com.kidslab.pocketphysics.domain.model.SensorType
import com.kidslab.pocketphysics.domain.usecase.RotationAnalysisUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val MAX_MUESTRAS_GRAFICO = 120
private const val OBJETIVO_GRADOS = 90f
private const val TOLERANCIA_GRADOS = 25f

data class RotationLabUiState(
    val disponible: Boolean = true,
    val midiendo: Boolean = false,
    val historialMagnitud: List<Float> = emptyList(),
    val anguloEstimado: Float = 0f,
    val catalogo: List<com.kidslab.pocketphysics.data.local.entity.SensorExperiment> = emptyList(),
    val experimentoKey: String? = null,
    val prediccion: String = "",
    val resultadoTexto: String? = null,
    val insigniaGanada: Boolean = false
)

class RotationLabViewModel(
    private val sensorRepository: SensorRepository,
    private val profileRepository: ProfileRepository,
    private val experimentRepository: ExperimentRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RotationLabUiState(disponible = sensorRepository.isSensorAvailable(SensorType.GYROSCOPE))
    )
    val uiState: StateFlow<RotationLabUiState> = _uiState.asStateFlow()

    private var medicionJob: Job? = null
    private val muestrasSesion = mutableListOf<RotationSample>()

    init {
        viewModelScope.launch {
            experimentRepository.observeCatalogByLab("rotacion").collect { lista ->
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
        _uiState.value = _uiState.value.copy(midiendo = true, historialMagnitud = emptyList(), anguloEstimado = 0f, resultadoTexto = null)
        medicionJob = viewModelScope.launch {
            sensorRepository.observeGyroscope().collect { sample ->
                muestrasSesion.add(sample)
                val nuevoHistorial = (_uiState.value.historialMagnitud + sample.magnitude).takeLast(MAX_MUESTRAS_GRAFICO)
                _uiState.value = _uiState.value.copy(
                    historialMagnitud = nuevoHistorial,
                    anguloEstimado = RotationAnalysisUseCase.anguloEstimadoGrados(muestrasSesion)
                )
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

            val summary = RotationAnalysisUseCase.summarize(muestras)
            val angulo = RotationAnalysisUseCase.anguloEstimadoGrados(muestras)
            val prediccionTexto = _uiState.value.prediccion.ifBlank { "(sin predicción escrita)" }

            val cercaDe90 = kotlin.math.abs(angulo - OBJETIVO_GRADOS) <= TOLERANCIA_GRADOS
            val textoResultado = "El giro estimado fue de ${"%.0f".format(angulo)} grados aproximadamente."
            val conclusion = if (cercaDe90) {
                "¡Muy cerca de un giro de 90°! El giroscopio mide velocidad de giro, no el ángulo directamente."
            } else {
                "El giroscopio mide velocidad de giro (rad/s); el ángulo se calcula sumándola en el tiempo, así que es una estimación."
            }

            experimentRepository.saveSession(
                profileId = profileId,
                experimentKey = experimentKey,
                sensorType = SensorType.GYROSCOPE.name,
                predictionText = prediccionTexto,
                summary = summary,
                prediccionCorrecta = true,
                textoResultado = textoResultado,
                textoConclusion = conclusion
            )

            var insigniaGanada = false
            if (cercaDe90) {
                insigniaGanada = challengeRepository.grantBadgeIfNew(profileId, "badge_maestro_giro")
            }

            _uiState.value = _uiState.value.copy(resultadoTexto = "$textoResultado $conclusion", insigniaGanada = insigniaGanada)
        }
    }

    override fun onCleared() {
        super.onCleared()
        medicionJob?.cancel()
    }
}
