package io.github.illusion.mobileapp.ui.screens.themes

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun appTypography() = Typography(

    headlineLarge = TextStyle(
        fontFamily = interFont(),
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp,
        lineHeight = 36.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = interFont(),
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 20.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = interFont(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 18.sp
    ),

    labelLarge = TextStyle(
        fontFamily = interFont(),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
)