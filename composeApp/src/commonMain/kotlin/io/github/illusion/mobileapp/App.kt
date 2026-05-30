package io.github.illusion.mobileapp

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import io.github.illusion.mobileapp.ui.screens.NavigationGraph
import io.github.illusion.mobileapp.ui.screens.themes.AppTheme
import io.github.illusion.mobileapp.ui.screens.themes.LocalFontScale
import io.github.illusion.mobileapp.service.rememberServiceStarter

@Composable
fun App() {
    BoxWithConstraints {
        val scale = (maxWidth / 392.dp).coerceIn(0.75f, 1.4f)

        val serviceStarter = rememberServiceStarter()

        CompositionLocalProvider(LocalFontScale provides scale) {
            AppTheme(fontScale = scale) {
                NavigationGraph(startService = serviceStarter)
            }
        }
    }
}