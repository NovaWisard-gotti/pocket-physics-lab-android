package com.kidslab.pocketphysics.ui.screens.light

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidslab.pocketphysics.data.repository.ChallengeRepository
import com.kidslab.pocketphysics.data.repository.ExperimentRepository
import com.kidslab.pocketphysics.data.repository.ProfileRepository
import com.kidslab.pocketphysics.data.repository.SensorRepository
import com.kidslab.pocketphysics.domain.model.LightSample
import com.kidslab.pocketphysics.domain.model.SensorType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val MAX_MUESTRAS_GRAFICO = 100

data class LightLabUiState(
    val disponible: Boolean = true,
    val midiendo: Boolean = false,
    val luxActual: Float = 0f,
    val historialLux: List<Float> = emptyList(),
    val luxHabitacion: Float? = null,
    val luxSombra: Float? = null,
    val catalogo: List<com.kidslab.pocketphysics.data.local.entity.SensorExperiment> = emptyList(),
    val experimentoKey: String? = null,
    val prediccion: String = "",
    val resultadoTexto: String? = null,
    val insigniaGanada: Boolean = false
)

class LightLabViewModel(
    private val sensorRepository: SensorRepository,
    private val profileRepository: ProfileRepository,
    private val experimentRepository: ExperimentRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LightLabUiState(disponible = sensorRepository.isSensorAvailable(SensorType.LIGHT))
    )
    val uiState: StateFlow<LightLabUiState> = _uiState.asStateFlow()

    private var medicionJob: Job? = null
    private val muestrasSesion = mutableListOf<LightSample>()

    init {
        viewModelScope.launch {
            experimentRepository.observeCatalogByLab("luz").collect { lista ->
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
        medicionJob?.cancel()
        medicionJob = null
        muestrasSesion.clear()
        _uiState.value = _uiState.value.copy(
            midiendo = false,
            luxActual = 0f,
            historialLux = emptyList(),
            luxHabitacion = null,
            luxSombra = null,
            prediccion = "",
            resultadoTexto = null,
            insigniaGanada = false
        )
    }

    fun onPrediccionChanged(texto: String) {
        _uiState.value = _uiState.value.copy(prediccion = texto)
    }

    fun iniciarMedicion() {
        if (!_uiState.value.disponible || medicionJob != null) return
        muestrasSesion.clear()
        _uiState.value = _uiState.value.copy(midiendo = true, historialLux = emptyList(), resultadoTexto = null)
        medicionJob = viewModelScope.launch {
            sensorRepository.observeLight().collect { sample ->
                muestrasSesion.add(sample)
                val nuevoHistorial = (_uiState.value.historialLux + sample.lux).takeLast(MAX_MUESTRAS_GRAFICO)
                _uiState.value = _uiState.value.copy(luxActual = sample.lux, historialLux = nuevoHistorial)
            }
        }
    }

    fun registrarComoHabitacion() {
        _uiState.value = _uiState.value.copy(luxHabitacion = _uiState.value.luxActual)
    }

    fun registrarComoSombra() {
        _uiState.value = _uiState.value.copy(luxSombra = _uiState.value.luxActual)
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

            val lux = muestras.map { it.lux }
            val summary = com.kidslab.pocketphysics.domain.model.SensorSummary(
                minimo = lux.min(),
                maximo = lux.max(),
                promedio = lux.average().toFloat(),
                duracionMs = (muestras.last().timestampMs - muestras.first().timestampMs).coerceAtLeast(0),
                muestras = muestras.size
            )
            val prediccionTexto = _uiState.value.prediccion.ifBlank { "(sin predicción escrita)" }

            val habitacion = _uiState.value.luxHabitacion
            val sombra = _uiState.value.luxSombra
            val textoResultado = if (habitacion != null && sombra != null) {
                "Luz de la habitación: ${"%.0f".format(habitacion)} lux. Con sombra de la mano: ${"%.0f".format(sombra)} lux."
            } else {
                "Luz medida: promedio ${"%.0f".format(summary.promedio)} lux (mínimo ${"%.0f".format(summary.minimo)}, máximo ${"%.0f".format(summary.maximo)})."
            }
            val conclusion = "El sensor de luz reacciona a la cantidad de luz visible que llega a la pantalla."

            experimentRepository.saveSession(
                profileId = profileId,
                experimentKey = experimentKey,
                sensorType = SensorType.LIGHT.name,
                predictionText = prediccionTexto,
                summary = summary,
                prediccionCorrecta = true,
                textoResultado = textoResultado,
                textoConclusion = conclusion
            )

            var insigniaGanada = false
            if (habitacion != null && sombra != null && habitacion != sombra) {
                insigniaGanada = challengeRepository.grantBadgeIfNew(profileId, "badge_cazador_de_luz")
            }

            _uiState.value = _uiState.value.copy(resultadoTexto = "$textoResultado $conclusion", insigniaGanada = insigniaGanada)
        }
    }

    override fun onCleared() {
        super.onCleared()
        medicionJob?.cancel()
    }
}
