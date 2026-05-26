package io.github.illusion.mobileapp.ui.screens.themes

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun appTypography(fontScale: Float = 1f) = Typography(

    headlineLarge = TextStyle(
        fontFamily = interFont(),
        fontWeight = FontWeight.Bold,
        fontSize = (64 * fontScale).sp,
        lineHeight = (36 * fontScale).sp
    ),

    bodyLarge = TextStyle(
        fontFamily = interFont(),
        fontWeight = FontWeight.Normal,
        fontSize = (20 * fontScale).sp,
        lineHeight = (20 * fontScale).sp
    ),

    bodyMedium = TextStyle(
        fontFamily = interFont(),
        fontWeight = FontWeight.Normal,
        fontSize = (16 * fontScale).sp,
        lineHeight = (18 * fontScale).sp
    ),

    labelLarge = TextStyle(
        fontFamily = interFont(),
        fontWeight = FontWeight.Bold,
        fontSize = (24 * fontScale).sp
    )
)
