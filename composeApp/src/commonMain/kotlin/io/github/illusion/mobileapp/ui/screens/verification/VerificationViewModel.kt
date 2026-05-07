package io.github.illusion.mobileapp.ui.screens.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.illusion.mobileapp.domain.usecase.RegisterUserUseCase
import io.github.illusion.mobileapp.domain.usecase.VerificationUserUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class VerificationViewModel(private val verificationUserUseCase: VerificationUserUseCase) : ViewModel() {

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

            while(verificationUserUseCase(_uiState.value.email).isFailure) {
                delay(5000.milliseconds)
            }
            onVerified()
        }
    }

    fun resendVerificationEmail(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // TODO: Заменить на реальный API вызов

            // Временная заглушка
            onSuccess()
            showError("Письмо отправлено повторно! Проверьте почту")
        }
    }
}