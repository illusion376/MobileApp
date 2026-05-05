package io.github.illusion.mobileapp.ui.screens.register

data class RegisterUIState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isTermsAccepted: Boolean = false,
    val isRegisterEnabled: Boolean = false,
    val errorMessage: String? = null
)