package io.github.illusion.mobileapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.illusion.mobileapp.domain.repository.KSafeRepository
import io.github.illusion.mobileapp.domain.usecase.LoginUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val kSafeRepository: KSafeRepository
) : ViewModel() {

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

            val email = _uiState.value.login
            val password = _uiState.value.password

            loginUserUseCase(email, password)
                .onSuccess {
                    if (_uiState.value.isRememberMe) {
                        kSafeRepository.saveData("remember_me", "true")
                    } else {
                        kSafeRepository.saveData("remember_me", "false")
                    }
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(errorMessage = error.message ?: "Неизвестная ошибка") }
                }
        }
    }
}