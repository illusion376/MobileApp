package io.github.illusion.mobileapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.illusion.mobileapp.ui.screens.login.LoginScreen
import io.github.illusion.mobileapp.ui.screens.register.RegisterScreen

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
}

@Composable
fun NavigationGraph() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

    when (currentScreen) {
        is Screen.Login -> {
            LoginScreen(
                onNavigateToRegister = { currentScreen = Screen.Register }
            )
        }
        is Screen.Register -> {
            RegisterScreen(
                onBackToLogin = { currentScreen = Screen.Login }
            )
        }
    }
}