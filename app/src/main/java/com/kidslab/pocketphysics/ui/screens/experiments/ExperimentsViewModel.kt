package com.kidslab.pocketphysics.ui.screens.experiments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidslab.pocketphysics.data.local.entity.SensorExperiment
import com.kidslab.pocketphysics.data.repository.ExperimentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExperimentsUiState(
    val experimentos: List<SensorExperiment> = emptyList(),
    val cargando: Boolean = true
)

class ExperimentsViewModel(private val repository: ExperimentRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ExperimentsUiState())
    val uiState: StateFlow<ExperimentsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeCatalog().collect { lista ->
                _uiState.value = ExperimentsUiState(experimentos = lista, cargando = false)
            }
        }
    }
}
