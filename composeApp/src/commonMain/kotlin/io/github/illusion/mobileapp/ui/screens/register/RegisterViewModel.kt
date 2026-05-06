package io.github.illusion.mobileapp.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update { state ->
            state.copy(
                email = email,
                isRegisterEnabled = validateForm(
                    email = email,
                    username = state.username,
                    password = state.password,
                    confirmPassword = state.confirmPassword,
                    isTermsAccepted = state.isTermsAccepted
                )
            )
        }
    }

    fun updateUsername(username: String) {
        _uiState.update { state ->
            state.copy(
                username = username,
                isRegisterEnabled = validateForm(
                    email = state.email,
                    username = username,
                    password = state.password,
                    confirmPassword = state.confirmPassword,
                    isTermsAccepted = state.isTermsAccepted
                )
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update { state ->
            state.copy(
                password = password,
                isRegisterEnabled = validateForm(
                    email = state.email,
                    username = state.username,
                    password = password,
                    confirmPassword = state.confirmPassword,
                    isTermsAccepted = state.isTermsAccepted
                )
            )
        }

        checkPasswordsMatch()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { state ->
            state.copy(
                confirmPassword = confirmPassword,
                isRegisterEnabled = validateForm(
                    email = state.email,
                    username = state.username,
                    password = state.password,
                    confirmPassword = confirmPassword,
                    isTermsAccepted = state.isTermsAccepted
                )
            )
        }

        checkPasswordsMatch()
    }

    fun updateTermsAccepted(isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                isTermsAccepted = isChecked,
                isRegisterEnabled = validateForm(
                    email = state.email,
                    username = state.username,
                    password = state.password,
                    confirmPassword = state.confirmPassword,
                    isTermsAccepted = isChecked
                )
            )
        }
    }

    private fun checkPasswordsMatch() {
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        if (password.isNotBlank() && confirmPassword.isNotBlank()) {
            if (password != confirmPassword) {
                _uiState.update { state ->
                    state.copy(errorMessage = "Пароли не совпадают")
                }
            } else {
                if (_uiState.value.errorMessage == "Пароли не совпадают") {
                    _uiState.update { state ->
                        state.copy(errorMessage = null)
                    }
                }
            }
        }
    }

    private fun validateForm(
        email: String,
        username: String,
        password: String,
        confirmPassword: String,
        isTermsAccepted: Boolean
    ): Boolean {
        return email.isNotBlank() &&
                username.isNotBlank() &&
                password.isNotBlank() &&
                password == confirmPassword &&
                isTermsAccepted
    }

    fun clearError() {
        _uiState.update { state ->
            state.copy(errorMessage = null)
        }
    }

    fun onRegisterClick(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(errorMessage = null)
            }

            // TODO: Заменить на реальный API вызов

            // Временная заглушка для тестирования
            val email = _uiState.value.email
            val username = _uiState.value.username
            val password = _uiState.value.password
            val confirmPassword = _uiState.value.confirmPassword

            if (email.isBlank() || username.isBlank() || password.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Заполните все поля") }
                return@launch
            }

            if (!email.contains("@")) {
                _uiState.update { it.copy(errorMessage = "Введите корректный email") }
                return@launch
            }

            if (password.length < 4) {
                _uiState.update { it.copy(errorMessage = "Пароль должен быть не менее 4 символов") }
                return@launch
            }

            if (password != confirmPassword) {
                _uiState.update { it.copy(errorMessage = "Пароли не совпадают") }
                return@launch
            }

            onSuccess(email)
        }
    }
}