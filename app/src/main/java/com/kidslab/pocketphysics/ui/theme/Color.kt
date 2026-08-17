package com.kidslab.pocketphysics.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta base (vivos, altos en saturación para captar la atención de niños)
val PhysicsBlue = Color(0xFF3E7BFA)
val PhysicsBlueDark = Color(0xFF1E4FA3)
val PhysicsCyan = Color(0xFF3DDBFF)
val PhysicsOrange = Color(0xFFFF9E3D)
val PhysicsCoral = Color(0xFFFF6B6B)
val PhysicsGreen = Color(0xFF3DD68C)
val PhysicsGreenDark = Color(0xFF1FA96A)
val PhysicsPurple = Color(0xFF9D6FFF)
val PhysicsPink = Color(0xFFFF6FD8)
val PhysicsYellow = Color(0xFFFFD34D)
val PhysicsYellowDeep = Color(0xFFFFB300)
val PhysicsRedSoft = Color(0xFFFF5A5F)

val PhysicsBackground = Color(0xFFF3F1FF)
val PhysicsBackgroundAlt = Color(0xFFEAF6FF)
val PhysicsSurface = Color(0xFFFFFFFF)
val PhysicsText = Color(0xFF1B2430)
val PhysicsTextSoft = Color(0xFF5B6472)

/** Gradiente de fondo general de la app: violeta suave -> celeste suave. */
val AppBackgroundGradient = listOf(PhysicsBackground, PhysicsBackgroundAlt)

/** Un gradiente vivo por cada laboratorio, usado en encabezados, tarjetas e íconos. */
enum class LabGradient(val emoji: String, val colors: List<Color>) {
    MOVIMIENTO("🚀", listOf(PhysicsBlue, PhysicsCyan)),
    ROTACION("🌀", listOf(PhysicsPurple, PhysicsPink)),
    SONIDO("🎵", listOf(PhysicsOrange, PhysicsCoral)),
    LUZ("💡", listOf(PhysicsYellowDeep, PhysicsGreen));

    companion object {
        fun forLabKey(labKey: String): LabGradient = when (labKey) {
            "movimiento" -> MOVIMIENTO
            "rotacion" -> ROTACION
            "sonido" -> SONIDO
            "luz" -> LUZ
            else -> MOVIMIENTO
        }
    }
}
