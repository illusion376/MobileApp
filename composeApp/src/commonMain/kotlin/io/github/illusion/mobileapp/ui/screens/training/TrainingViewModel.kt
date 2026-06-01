package io.github.illusion.mobileapp.ui.screens.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.illusion.mobileapp.domain.haptic.HapticFeedback
import io.github.illusion.mobileapp.domain.health.StepCounter
import io.github.illusion.mobileapp.domain.location.LocationProvider
import io.github.illusion.mobileapp.service.ServiceStarter
import io.github.illusion.mobileapp.service.TrainingServiceActions
import io.github.illusion.mobileapp.service.TrainingServiceBridge
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val STEP_LENGTH_KM = 0.00075

class TrainingViewModel(
    private val stepCounter: StepCounter,
    private val serviceStarter: ServiceStarter,
    private val locationProvider: LocationProvider,
    private val hapticFeedback: HapticFeedback,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingUIState())
    val uiState: StateFlow<TrainingUIState> = _uiState.asStateFlow()

    private var locationJob: Job? = null

    init {
        TrainingServiceBridge.onStepsChanged = { steps ->
            _uiState.update {
                it.copy(steps = steps, distanceKm = steps * STEP_LENGTH_KM)
            }
        }
        TrainingServiceBridge.onSecondsChanged = { seconds ->
            _uiState.update { it.copy(durationSeconds = seconds) }
        }
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        locationJob = viewModelScope.launch {
            locationProvider.startUpdates()
                .catch { }
                .collect { (lat, lng) ->
                    _uiState.update { state ->
                        val newRoute = if (state.isRunning && !state.isPaused)
                            state.routePoints + (lat to lng)
                        else
                            state.routePoints
                        state.copy(
                            userLatitude = lat,
                            userLongitude = lng,
                            routePoints = newRoute,
                        )
                    }
                }
        }
    }

    fun toggleTraining() {
        if (_uiState.value.isRunning) stopTraining() else startTraining()
    }

    fun togglePause() {
        if (!_uiState.value.isRunning) return
        if (_uiState.value.isPaused) {
            _uiState.update { it.copy(isPaused = false) }
            serviceStarter.start(TrainingServiceActions.ACTION_RESUME)
        } else {
            _uiState.update { it.copy(isPaused = true) }
            serviceStarter.start(TrainingServiceActions.ACTION_PAUSE)
        }
    }

    private fun startTraining() {
        try {
            hapticFeedback.performPaymentImpact()
        } catch (_: Exception) { }
        _uiState.update { it.copy(isRunning = true, isPaused = false, routePoints = emptyList()) }
        serviceStarter.start(TrainingServiceActions.ACTION_START)
    }

    private fun stopTraining() {
        serviceStarter.start(TrainingServiceActions.ACTION_STOP)
        _uiState.update { TrainingUIState() }
    }

    fun selectTab(tab: TrainingTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        _uiState.update { state ->
            val newRoute = if (state.isRunning && !state.isPaused)
                state.routePoints + (latitude to longitude)
            else
                state.routePoints
            state.copy(
                userLatitude = latitude,
                userLongitude = longitude,
                routePoints = newRoute,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationJob?.cancel()
        locationProvider.stop()
        TrainingServiceBridge.onStepsChanged = null
        TrainingServiceBridge.onSecondsChanged = null
    }
}
