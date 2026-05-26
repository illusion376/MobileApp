package io.github.illusion.mobileapp.ui.screens.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrainingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingUIState())
    val uiState: StateFlow<TrainingUIState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startTraining() {
        if (_uiState.value.isRunning) return
        _uiState.update { it.copy(isRunning = true, isPaused = false) }
        startTimer()
    }

    fun togglePause() {
        val current = _uiState.value
        if (!current.isRunning) return
        if (current.isPaused) {
            _uiState.update { it.copy(isPaused = false) }
            startTimer()
        } else {
            timerJob?.cancel()
            _uiState.update { it.copy(isPaused = true) }
        }
    }

    fun toggleTraining() {
        if (_uiState.value.isRunning) {
            stopTraining()
        } else {
            startTraining()
        }
    }

    private fun stopTraining() {
        timerJob?.cancel()
        _uiState.update {
            it.copy(isRunning = false, isPaused = false)
        }
    }

    fun selectTab(tab: TrainingTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        _uiState.update { state ->
            val newRoute = if (state.isRunning && !state.isPaused) {
                state.routePoints + (latitude to longitude)
            } else {
                state.routePoints
            }
            state.copy(
                userLatitude = latitude,
                userLongitude = longitude,
                routePoints = newRoute,
            )
        }
    }

    fun updateSteps(steps: Int) {
        _uiState.update { it.copy(steps = steps) }
    }

    fun updateDistance(km: Double) {
        _uiState.update { it.copy(distanceKm = km) }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (!_uiState.value.isPaused) {
                    _uiState.update { it.copy(durationSeconds = it.durationSeconds + 1) }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
