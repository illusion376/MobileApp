package io.github.illusion.mobileapp.ui.screens.main

import androidx.compose.runtime.Immutable
import io.github.illusion.mobileapp.domain.model.Player
import kotlin.Int

@Immutable
data class MainUIState(
    val level: Int = 1,
    val nextLevel: Int = 2,
    val levelProgress: Float = 0f,
    val currentXp: Int = 0,
    val xpToNextLevel: Int = 1,
    val todaySteps: Int = 0,
    val caloriesBurned: Int = 0,
    val walkMinutes: Int = 0,
    val dailyQuests: List<DailyQuest> = emptyList(),
    val stats: PlayerStats = PlayerStats(),
    val weeklyStreakDone: List<Boolean> = List(7) { false },
    val currentWeekdayIndex: Int = 0,
    val streakDays: Int = 0,
    val disciplineTitle: String = "ДИСЦИПЛИНА = СИЛА",
    val disciplineSubtitle: String = "Каждый шаг приближает\nтебя к лучшей версии себя.",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    companion object {
        fun fromCharacter(player : Player): MainUIState {
            return MainUIState(
                level = player.level,
                nextLevel = player.level,
                xpToNextLevel = player.experienceToNextLevel,
                currentXp = player.experience,
                todaySteps = player.steps,
                stats = PlayerStats(player.strength, player.vitality, player.stamina),
                streakDays = player.streakDays,
                weeklyStreakDone = loadStreakDays(player.streakDays)
            )
        }
    }
    val xpProgress: Float
        get() = safeFraction(currentXp, xpToNextLevel)
}

@Immutable
data class DailyQuest(
    val id: String,
    val type: QuestType,
    val title: String,
    val current: Int,
    val target: Int,
    val rewardXp: Int,
) {
    val progress: Float
        get() = safeFraction(current, target)
}

enum class QuestType { STEPS, CALORIES, WORKOUTS }

@Immutable
data class PlayerStats(
    val strength: Int = 0,
    val vitality: Int = 0,
    val stamina: Int = 0,
)

private fun safeFraction(current: Int, target: Int): Float =
    if (target <= 0) 0f else (current.toFloat() / target.toFloat()).coerceIn(0f, 1f)

private fun loadStreakDays(streakDays: Int): List<Boolean> {
    val list = mutableListOf(false, false, false, false, false, false, false)

    for (i in 0 until (streakDays % 7)) {
        list[i] = true
    }

    return list
}