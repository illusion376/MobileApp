package io.github.illusion.mobileapp.ui.screens.quests

import androidx.compose.runtime.Immutable

@Immutable
data class QuestsUIState(
    val selectedTab: QuestsTab = QuestsTab.AVAILABLE,
    val quests: List<QuestItem> = emptyList(),
    val weeklyGoal: WeeklyGoal = WeeklyGoal(),
    val isLoading: Boolean = false,
)

enum class QuestsTab { AVAILABLE, ACTIVE }

@Immutable
data class QuestItem(
    val id: String,
    val title: String,
    val description: String,
    val iconType: QuestIconType,
    val current: Float,
    val target: Float,
    val unit: String = "",
    val rewardXp: Int,
    val isActive: Boolean = false,
    val isCompleted: Boolean = false,
    val isStreak: Boolean = false,
    val streakDays: Int = 0,
    val streakTarget: Int = 0,
) {
    val progress: Float
        get() = if (!isActive || target <= 0f) 0f else (current / target).coerceIn(0f, 1f)

    val progressText: String
        get() = if (!isActive) "" else if (unit.isNotEmpty()) {
            "${formatNumber(current)} / ${formatNumber(target)} $unit"
        } else {
            "${current.toInt()} / ${target.toInt()}"
        }
}

enum class QuestIconType { BOOT, FIRE, SHIELD, TARGET }

@Immutable
data class WeeklyGoal(
    val completedQuests: Int = 0,
    val totalQuests: Int = 5,
    val bonusXp: Int = 500,
) {
    val dots: List<Boolean>
        get() = List(totalQuests) { it < completedQuests }
}

private fun formatNumber(value: Float): String {
    return if (value == value.toInt().toFloat()) {
        value.toInt().toString()
    } else {
        val intPart = value.toInt()
        val decPart = ((value - intPart) * 10).toInt()
        "$intPart.$decPart"
    }
}
