package io.github.illusion.mobileapp.ui.screens.quests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.illusion.mobileapp.data.remote.api.QuestApi
import io.github.illusion.mobileapp.data.remote.dto.QuestDTO
import io.github.illusion.mobileapp.domain.repository.KSafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestsViewModel(
    private val questApi: QuestApi,
    private val kSafeRepository: KSafeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestsUIState(isLoading = true))
    val uiState: StateFlow<QuestsUIState> = _uiState.asStateFlow()

    init {
        loadQuests()
    }

    fun selectTab(tab: QuestsTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun activateQuest(questId: String) {
        viewModelScope.launch {
            val userId = kSafeRepository.getDataOrNull("userId") ?: return@launch
            try {
                questApi.activateQuest(userId, questId)
                loadQuests()
            } catch (_: Exception) {
            }
        }
    }

    fun loadQuests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userId = kSafeRepository.getDataOrNull("userId") ?: "guest"
            try {
                val dtos = questApi.getQuests(userId)
                val quests = dtos.map { it.toQuestItem() }
                val completed = quests.count { it.isActive && it.isCompleted }
                _uiState.update {
                    it.copy(
                        quests = quests,
                        weeklyGoal = WeeklyGoal(
                            completedQuests = completed.coerceAtMost(5),
                            totalQuests = 5,
                            bonusXp = 500,
                        ),
                        isLoading = false,
                    )
                }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun QuestDTO.toQuestItem(): QuestItem {
        val iconType = when (this.iconType) {
            "BOOT" -> QuestIconType.BOOT
            "FIRE" -> QuestIconType.FIRE
            "SHIELD" -> QuestIconType.SHIELD
            "TARGET" -> QuestIconType.TARGET
            else -> QuestIconType.BOOT
        }
        return QuestItem(
            id = id,
            title = title,
            description = description,
            iconType = iconType,
            current = progress,
            target = target,
            unit = unit,
            rewardXp = rewardXp,
            isActive = isActive,
            isCompleted = isCompleted,
            isStreak = isStreak,
            streakDays = if (isStreak) progress.toInt() else 0,
            streakTarget = streakTarget,
        )
    }
}
