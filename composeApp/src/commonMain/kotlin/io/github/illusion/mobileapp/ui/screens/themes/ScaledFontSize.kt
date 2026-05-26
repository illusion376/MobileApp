package io.github.illusion.mobileapp.ui.screens.themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val LocalFontScale = staticCompositionLocalOf { 1f }

@Composable
@ReadOnlyComposable
fun scaledSp(value: Int): TextUnit = (value * LocalFontScale.current).sp

@Composable
@ReadOnlyComposable
fun scaledSp(value: Float): TextUnit = (value * LocalFontScale.current).sp
