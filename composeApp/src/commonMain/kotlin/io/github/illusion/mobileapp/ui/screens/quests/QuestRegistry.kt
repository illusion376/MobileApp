package io.github.illusion.mobileapp.ui.screens.quests

import io.github.illusion.mobileapp.domain.model.Player

/**
 * Реестр квестов.
 *
 * Чтобы добавить новый квест:
 * 1. Создай объект, наследующий QuestDefinition
 * 2. Добавь его в список ALL внизу файла
 *
 * Пример:
 * ```
 * object MarathonQuest : QuestDefinition(
 *     id = "marathon",
 *     title = "МАРАФОНЕЦ",
 *     description = "Пройди 42 км за месяц",
 *     iconType = QuestIconType.TARGET,
 *     target = 42f,
 *     unit = "км",
 *     rewardXp = 500,
 * ) {
 *     override fun computeCurrent(player: Player): Float =
 *         (player.steps / 1300f).coerceAtMost(target)
 * }
 * ```
 */

abstract class QuestDefinition(
    val id: String,
    val title: String,
    val description: String,
    val iconType: QuestIconType,
    val target: Float,
    val unit: String = "",
    val rewardXp: Int,
    val isStreak: Boolean = false,
    val streakTarget: Int = 0,
) {
    abstract fun computeCurrent(player: Player): Float

    open fun computeStreakDays(player: Player): Int = 0

    fun toQuestItem(player: Player, isActive: Boolean): QuestItem {
        val current = if (isActive) computeCurrent(player) else 0f
        return QuestItem(
            id = id,
            title = title,
            description = description,
            iconType = iconType,
            current = current,
            target = target,
            unit = unit,
            rewardXp = rewardXp,
            isActive = isActive,
            isCompleted = isActive && current >= target,
            isStreak = isStreak,
            streakDays = if (isActive) computeStreakDays(player) else 0,
            streakTarget = streakTarget,
        )
    }
}

// ──────────────────────────────────────────────────────────────
// Определения квестов — добавляй новые объекты ниже
// ──────────────────────────────────────────────────────────────

object FirstStepsQuest : QuestDefinition(
    id = "first_steps",
    title = "ПЕРВЫЕ ШАГИ",
    description = "Пройди 1,000 шагов за один день",
    iconType = QuestIconType.BOOT,
    target = 1000f,
    rewardXp = 50,
) {
    override fun computeCurrent(player: Player): Float =
        player.steps.toFloat().coerceAtMost(target)
}

object DayStreakQuest : QuestDefinition(
    id = "day_streak",
    title = "СЕРИЯ ДНЯ",
    description = "Тренируйся 3 дня подряд",
    iconType = QuestIconType.FIRE,
    target = 3f,
    rewardXp = 100,
    isStreak = true,
    streakTarget = 3,
) {
    override fun computeCurrent(player: Player): Float =
        player.streakDays.toFloat().coerceAtMost(target)

    override fun computeStreakDays(player: Player): Int =
        player.streakDays.coerceAtMost(streakTarget)
}

object DisciplineQuest : QuestDefinition(
    id = "discipline",
    title = "СИЛА ДИСЦИПЛИНЫ",
    description = "Заверши 5 тренировок",
    iconType = QuestIconType.SHIELD,
    target = 5f,
    rewardXp = 200,
) {
    override fun computeCurrent(player: Player): Float =
        player.streakDays.coerceAtMost(5).toFloat()
}

object ExplorerQuest : QuestDefinition(
    id = "explorer",
    title = "ИССЛЕДОВАТЕЛЬ",
    description = "Пройди 10 км за неделю",
    iconType = QuestIconType.TARGET,
    target = 10f,
    unit = "км",
    rewardXp = 150,
) {
    override fun computeCurrent(player: Player): Float =
        (player.steps / 1300f).coerceAtMost(target)
}

// ──────────────────────────────────────────────────────────────
// Список всех квестов — добавь свой объект сюда
// ──────────────────────────────────────────────────────────────

object QuestRegistry {
    val ALL: List<QuestDefinition> = listOf(
        FirstStepsQuest,
        DayStreakQuest,
        DisciplineQuest,
        ExplorerQuest,
    )
}
