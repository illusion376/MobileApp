package io.github.illusion.mobileapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.illusion.mobileapp.ui.screens.login.LoginScreen
import io.github.illusion.mobileapp.ui.screens.register.RegisterScreen
import io.github.illusion.mobileapp.ui.screens.verification.VerificationScreen

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    data class Verification(val email: String) : Screen()
}

@Composable
fun NavigationGraph() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

    // Без анимаций - просто показываем нужный экран
    when (currentScreen) {
        is Screen.Login -> {
            LoginScreen(
                onNavigateToRegister = { currentScreen = Screen.Register }
            )
        }
        is Screen.Register -> {
            RegisterScreen(
                onBackToLogin = { currentScreen = Screen.Login },
                onNavigateToVerification = { email ->
                    currentScreen = Screen.Verification(email)
                }
            )
        }
        is Screen.Verification -> {
            VerificationScreen(
                email = (currentScreen as Screen.Verification).email,
                onVerificationComplete = {
                    // TODO: Переход на главный экран
                    currentScreen = Screen.Login
                },
                onBackToLogin = { currentScreen = Screen.Login }
            )
        }
    }
}