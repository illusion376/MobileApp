package io.github.illusion.mobileapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun updateLogin(login: String) {
        _uiState.update { state ->
            state.copy(
                login = login,
                isLoginEnabled = login.isNotBlank() && state.password.isNotBlank()
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update { state ->
            state.copy(
                password = password,
                isLoginEnabled = state.login.isNotBlank() && password.isNotBlank()
            )
        }
    }

    fun updateRememberMe(isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(isRememberMe = isChecked)
        }
    }

    fun clearError() {
        _uiState.update { state ->
            state.copy(errorMessage = null)
        }
    }

    fun onLoginClick(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(errorMessage = null) }

            // TODO: Заменить на реальный API вызов

            // Временная заглушка для тестирования
            val success = _uiState.value.login == "admin" && _uiState.value.password == "123"
            if (success) {
                if (_uiState.value.isRememberMe) {
                    // TODO: Сохранить логин и пароль
                }
                onSuccess()
            } else {
                _uiState.update { it.copy(errorMessage = "Неверный логин или пароль") }
            }
        }
    }
}