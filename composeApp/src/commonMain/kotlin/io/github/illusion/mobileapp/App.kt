package io.github.illusion.mobileapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.illusion.mobileapp.ui.screens.login.LoginScreen
import io.github.illusion.mobileapp.ui.screens.themes.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        LoginScreen()
    }
}