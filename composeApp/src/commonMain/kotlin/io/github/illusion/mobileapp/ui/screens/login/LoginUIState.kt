package io.github.illusion.mobileapp.ui.screens.login

data class LoginUIState(
    val showContent: Boolean = true,
    val login: String = "",
    val password: String = "",
    val isRememberMe: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val errorMessage: String? = null
)
