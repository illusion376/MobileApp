package io.github.illusion.mobileapp.ui.screens.themes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(fontScale: Float = 1f, content: @Composable () -> Unit) {
    MaterialTheme(
        typography = appTypography(fontScale),
        content = content
    )
}
