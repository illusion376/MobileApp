package io.github.illusion.mobileapp.ui.screens.verification

data class VerificationUIState(
    val email: String = "",
    val errorMessage: String? = null,
    val isVerified: Boolean = false
)