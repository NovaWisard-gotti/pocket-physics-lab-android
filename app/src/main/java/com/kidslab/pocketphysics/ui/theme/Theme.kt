package com.kidslab.pocketphysics.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = PhysicsBlue,
    onPrimary = Color.White,
    primaryContainer = PhysicsCyan.copy(alpha = 0.25f),
    secondary = PhysicsOrange,
    onSecondary = Color.White,
    secondaryContainer = PhysicsYellow.copy(alpha = 0.35f),
    tertiary = PhysicsPurple,
    onTertiary = Color.White,
    background = PhysicsBackground,
    surface = PhysicsSurface,
    surfaceVariant = Color(0xFFEFEBFF),
    onBackground = PhysicsText,
    onSurface = PhysicsText,
    onSurfaceVariant = PhysicsTextSoft,
    error = PhysicsRedSoft
)

private val DarkColors = darkColorScheme(
    primary = PhysicsCyan,
    secondary = PhysicsOrange,
    tertiary = PhysicsPink,
    background = Color(0xFF13172A),
    surface = Color(0xFF1E2340),
    surfaceVariant = Color(0xFF2A2F52),
    error = PhysicsRedSoft
)

val PhysicsTypography = Typography(
    displaySmall = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, letterSpacing = 0.2.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 27.sp, letterSpacing = 0.1.sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 17.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
)

val PhysicsShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
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
        shapes = PhysicsShapes,
        content = content
    )
}
