package com.kidslab.pocketphysics.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = PhysicsBlue,
    onPrimary = Color.White,
    secondary = PhysicsOrange,
    tertiary = PhysicsPurple,
    background = PhysicsBackground,
    surface = PhysicsSurface,
    onBackground = PhysicsText,
    onSurface = PhysicsText,
    error = PhysicsRedSoft
)

private val DarkColors = darkColorScheme(
    primary = PhysicsBlue,
    secondary = PhysicsOrange,
    tertiary = PhysicsPurple,
    background = PhysicsText,
    surface = PhysicsBlueDark,
    error = PhysicsRedSoft
)

val PhysicsTypography = Typography(
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp)
)

@Composable
fun PocketPhysicsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = PhysicsTypography,
        content = content
    )
}
