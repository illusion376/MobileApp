package io.github.illusion.mobileapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.illusion.mobileapp.domain.usecase.LoginUserUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUserUseCase: LoginUserUseCase) : ViewModel() {

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

            // Проверка логина и пароля
            val result = loginUserUseCase(_uiState.value.login, _uiState.value.password)

            if (result.isSuccess) {
                if (_uiState.value.isRememberMe) {
                    saveCredentials(_uiState.value.login, _uiState.value.password)
                }
                // TODO: Переход на главный экран
                println("Успешный вход!")
            } else {
                _uiState.update { state ->
                    state.copy(
                        errorMessage = "Неправильный логин или пароль"
                    )
                }
            }
        }
    }

    private fun saveCredentials(login: String, password: String) {
        // TODO: Сохранить в DataStore/SharedPreferences
        println("Сохранены данные: $login / $password")
    }
}