package com.kidslab.pocketphysics.ui.screens.sound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidslab.pocketphysics.data.repository.ChallengeRepository
import com.kidslab.pocketphysics.data.repository.ExperimentRepository
import com.kidslab.pocketphysics.data.repository.ProfileRepository
import com.kidslab.pocketphysics.data.repository.SoundRepository
import com.kidslab.pocketphysics.domain.model.SensorType
import com.kidslab.pocketphysics.domain.model.SensorSummary
import com.kidslab.pocketphysics.domain.model.SoundAnalysisResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val MAX_BLOQUES_SESION = 60

data class SoundLabUiState(
    val disponible: Boolean = true,
    val permisoConcedido: Boolean = false,
    val permisoFueDenegado: Boolean = false,
    val escuchando: Boolean = false,
    val ultimoAnalisis: SoundAnalysisResult? = null,
    val historialAmplitud: List<Float> = emptyList(),
    val catalogo: List<com.kidslab.pocketphysics.data.local.entity.SensorExperiment> = emptyList(),
    val experimentoKey: String? = null,
    val prediccion: String = "",
    val resultadoTexto: String? = null,
    val insigniaGanada: Boolean = false
)

class SoundLabViewModel(
    private val soundRepository: SoundRepository,
    private val profileRepository: ProfileRepository,
    private val experimentRepository: ExperimentRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SoundLabUiState(disponible = soundRepository.isMicrophoneAvailable())
    )
    val uiState: StateFlow<SoundLabUiState> = _uiState.asStateFlow()

    private var escuchaJob: Job? = null
    private val bloquesSesion = mutableListOf<SoundAnalysisResult>()

    init {
        viewModelScope.launch {
            experimentRepository.observeCatalogByLab("sonido").collect { lista ->
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

    /** Borra el resultado actual para poder repetir la prueba desde cero. */
    fun reiniciarPrueba() {
        escuchaJob?.cancel()
        escuchaJob = null
        bloquesSesion.clear()
        _uiState.value = _uiState.value.copy(
            escuchando = false,
            ultimoAnalisis = null,
            historialAmplitud = emptyList(),
            prediccion = "",
            resultadoTexto = null,
            insigniaGanada = false
        )
    }

    fun onPrediccionChanged(texto: String) {
        _uiState.value = _uiState.value.copy(prediccion = texto)
    }

    /** El Activity/Composable llama esto tras pedir el permiso RECORD_AUDIO al sistema. */
    fun onPermisoResultado(concedido: Boolean) {
        _uiState.value = _uiState.value.copy(
            permisoConcedido = concedido,
            permisoFueDenegado = !concedido
        )
    }

    fun iniciarEscucha() {
        if (!_uiState.value.disponible || !_uiState.value.permisoConcedido || escuchaJob != null) return
        bloquesSesion.clear()
        _uiState.value = _uiState.value.copy(escuchando = true, historialAmplitud = emptyList(), resultadoTexto = null)
        escuchaJob = viewModelScope.launch {
            soundRepository.observeSoundAnalysis().collect { analisis ->
                bloquesSesion.add(analisis)
                val nuevoHistorial = (_uiState.value.historialAmplitud + analisis.amplitudPromedio).takeLast(MAX_BLOQUES_SESION)
                _uiState.value = _uiState.value.copy(ultimoAnalisis = analisis, historialAmplitud = nuevoHistorial)
            }
        }
    }

    fun detenerEscuchaYGuardar() {
        escuchaJob?.cancel()
        escuchaJob = null
        _uiState.value = _uiState.value.copy(escuchando = false)

        val experimentKey = _uiState.value.experimentoKey ?: return
        val bloques = bloquesSesion.toList()
        if (bloques.isEmpty()) return

        viewModelScope.launch {
            val profileId = profileRepository.observeProfile().first()?.id ?: return@launch

            val amplitudes = bloques.map { it.amplitudPromedio }
            val frecuencias = bloques.map { it.frecuenciaDominanteHz }
            val summary = SensorSummary(
                minimo = amplitudes.min(),
                maximo = amplitudes.max(),
                promedio = amplitudes.average().toFloat(),
                duracionMs = bloques.size * 128L, // aproximado, en bloques de análisis
                muestras = bloques.size
            )
            val prediccionTexto = _uiState.value.prediccion.ifBlank { "(sin predicción escrita)" }
            val frecuenciaPromedio = frecuencias.average().toFloat()

            val textoResultado = "Amplitud promedio: ${"%.0f".format(summary.promedio)}. Frecuencia dominante aproximada: ${"%.0f".format(frecuenciaPromedio)} Hz."
            val conclusion = "Recuerda: esto es una comparación educativa, no una medición de laboratorio profesional."

            experimentRepository.saveSession(
                profileId = profileId,
                experimentKey = experimentKey,
                sensorType = SensorType.MICROPHONE.name,
                predictionText = prediccionTexto,
                summary = summary,
                prediccionCorrecta = true,
                textoResultado = textoResultado,
                textoConclusion = conclusion
            )

            var insigniaGanada = false
            if (experimentKey == "son_suave_vs_fuerte" || experimentKey == "son_grave_vs_agudo") {
                insigniaGanada = challengeRepository.grantBadgeIfNew(profileId, "badge_dj_de_sonidos")
            }

            _uiState.value = _uiState.value.copy(resultadoTexto = "$textoResultado $conclusion", insigniaGanada = insigniaGanada)
        }
    }

    override fun onCleared() {
        super.onCleared()
        escuchaJob?.cancel()
    }
}
