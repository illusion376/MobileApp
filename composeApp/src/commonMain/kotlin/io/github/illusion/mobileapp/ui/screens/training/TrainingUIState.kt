package io.github.illusion.mobileapp.ui.screens.training

import androidx.compose.runtime.Immutable

@Immutable
data class TrainingUIState(
    val distanceKm: Double = 0.0,
    val steps: Int = 0,
    val durationSeconds: Int = 0,
    val isPaused: Boolean = false,
    val isRunning: Boolean = false,
    val userLatitude: Double = 55.7558,
    val userLongitude: Double = 37.6173,
    val routePoints: List<Pair<Double, Double>> = emptyList(),
    val selectedTab: TrainingTab = TrainingTab.LOCATION,
    val completion: TrainingResult? = null,
) {
    val durationMinutes: Int get() = durationSeconds / 60
    val durationRemainderSeconds: Int get() = durationSeconds % 60
}

@Immutable
data class TrainingResult(
    val gainedXp: Int,
    val level: Int,
    val nextLevel: Int,
    val currentXp: Int,
    val xpToNextLevel: Int,
    val previousProgress: Float = 0f,
) {
    val progress: Float =
        if (xpToNextLevel <= 0) 0f
        else (currentXp.toFloat() / xpToNextLevel).coerceIn(0f, 1f)
}

enum class TrainingTab {
    PAUSE, LOCATION, STATISTICS
}