package io.github.illusion.mobileapp.ui.screens.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerificationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VerificationUIState())
    val uiState: StateFlow<VerificationUIState> = _uiState.asStateFlow()

    fun setEmail(email: String) {
        _uiState.update { state ->
            state.copy(email = email)
        }
    }

    fun showError(message: String) {
        _uiState.update { state ->
            state.copy(errorMessage = message)
        }
    }

    fun clearError() {
        _uiState.update { state ->
            state.copy(errorMessage = null)
        }
    }

    fun checkVerificationStatus(onVerified: () -> Unit) {
        viewModelScope.launch {
            // Имитация проверки статуса подтверждения
            delay(3000)

            // В реальном проекте здесь был бы запрос к API
            val isVerified = false // Пока false для демонстрации

            if (isVerified) {
                _uiState.update { state ->
                    state.copy(isVerified = true)
                }
                onVerified()
            } else {
                checkVerificationStatus(onVerified)
            }
        }
    }

    fun resendVerificationEmail(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // Имитация отправки письма
            delay(1000)

            // В реальном проекте здесь был бы API запрос
            val success = true

            if (success) {
                onSuccess()
                showError("Письмо отправлено повторно! Проверьте почту")
            } else {
                showError("Не удалось отправить письмо. Попробуйте позже")
            }
        }
    }
}