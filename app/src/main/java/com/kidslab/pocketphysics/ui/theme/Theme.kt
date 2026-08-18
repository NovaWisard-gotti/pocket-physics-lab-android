package com.kidslab.pocketphysics.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
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

/**
 * Tema único de la app: siempre usa la paleta clara y vivosa diseñada para
 * niños, sin importar si el teléfono está en modo oscuro. Así los colores
 * (y su contraste con el texto) se ven siempre iguales y legibles.
 */
@Composable
fun PocketPhysicsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = PhysicsTypography,
        shapes = PhysicsShapes,
        content = content
    )
}
