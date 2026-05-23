package io.github.illusion.mobileapp.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.illusion.mobileapp.domain.usecase.GetCharacterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val getCharacterUseCase: GetCharacterUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<MainUIState> = _uiState.asStateFlow()


    init {
        loadCharacter()
    }

    fun onStartTrainingClick() {
        // TODO: hook up navigation / training start use-case
    }

    fun onInventoryClick() {
        // TODO: navigate to inventory
    }

    fun onQuestsClick() {
        // TODO: navigate to quests
    }

    fun onBossesClick() {
        // TODO: navigate to bosses
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun loadCharacter() {
        viewModelScope.launch {

            val character = getCharacterUseCase(19).getOrNull()
            if (character != null) {
                _uiState.value = MainUIState.fromCharacter(character)
            }
        }
    }

    private fun initialState(): MainUIState = MainUIState(
        isLoading = true,
        level = 0,
        nextLevel = 0,
        levelProgress = 0f,
        currentXp = 0,
        xpToNextLevel = 0,
        todaySteps = 0,
        weeklyStreakDone = listOf(false, false, false, false, false, false, false),
        currentWeekdayIndex = 0,
        streakDays = 0,
        stats = PlayerStats(
            strength = 0,
            vitality = 0,
            stamina = 0,
        ),
    )
}
