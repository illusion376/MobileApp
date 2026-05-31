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
    val streakDays: Int = 0
) {
    val durationMinutes: Int get() = durationSeconds / 60
    val durationRemainderSeconds: Int get() = durationSeconds % 60

    // Вспомогательное свойство: показывать плашку, только если серия больше 0 дней
    val showStreakBanner: Boolean get() = streakDays > 0
}

enum class TrainingTab {
    PAUSE, LOCATION, STATISTICS
}