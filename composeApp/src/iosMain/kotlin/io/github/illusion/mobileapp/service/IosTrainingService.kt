package io.github.illusion.mobileapp.service

import io.github.illusion.mobileapp.domain.health.StepCounter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IosTrainingService(private val stepCounter: StepCounter) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var stepJob: Job? = null
    private var timerJob: Job? = null

    private var currentSteps = 0
    private var currentSeconds = 0
    private var stepsOffset = 0

    fun handleAction(action: String) {
        when (action) {
            TrainingServiceActions.ACTION_START -> startTraining()
            TrainingServiceActions.ACTION_STOP -> stopTraining()
            TrainingServiceActions.ACTION_PAUSE -> pauseTraining()
            TrainingServiceActions.ACTION_RESUME -> resumeTraining()
        }
    }

    private fun startTraining() {
        currentSteps = 0
        currentSeconds = 0
        stepsOffset = 0
        startStepCounting()
        startTimer()
    }

    private fun pauseTraining() {
        stepsOffset = currentSteps
        stepJob?.cancel()
        stepCounter.stop()
        timerJob?.cancel()
    }

    private fun resumeTraining() {
        startStepCounting()
        startTimer()
    }

    private fun stopTraining() {
        currentSteps = 0
        currentSeconds = 0
        stepsOffset = 0
        stepJob?.cancel()
        timerJob?.cancel()
        stepCounter.stop()
    }

    private fun startStepCounting() {
        stepJob?.cancel()
        stepJob = scope.launch {
            stepCounter.start().collect { newSteps ->
                currentSteps = stepsOffset + newSteps
                TrainingServiceBridge.onStepsChanged?.invoke(currentSteps)
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            while (true) {
                delay(1000)
                currentSeconds++
                TrainingServiceBridge.onSecondsChanged?.invoke(currentSeconds)
            }
        }
    }
}