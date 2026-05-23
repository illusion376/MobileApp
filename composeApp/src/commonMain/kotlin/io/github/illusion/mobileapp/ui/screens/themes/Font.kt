package io.github.illusion.mobileapp.ui.screens.themes

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import io.github.illusion.mobileapp.resources.Res
import io.github.illusion.mobileapp.resources.inter_bold
import io.github.illusion.mobileapp.resources.inter_medium
import io.github.illusion.mobileapp.resources.inter_regular
import org.jetbrains.compose.resources.Font

@Composable
fun interFont() = FontFamily(
    Font(Res.font.inter_regular, weight = FontWeight.Normal),
    Font(Res.font.inter_medium, weight = FontWeight.Medium),
    Font(Res.font.inter_bold, weight = FontWeight.Bold)
)