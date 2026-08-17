package com.kidslab.pocketphysics.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

/**
 * Gráfico de línea simple dibujado a mano con Canvas de Compose.
 * No se añade ninguna librería de gráficos externa (como pide la
 * especificación); esto es suficiente para mostrar tendencias en vivo.
 *
 * @param values valores a graficar, en el rango aproximado [minValue, maxValue].
 */
@Composable
fun SimpleLineChart(
    values: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    minValue: Float? = null,
    maxValue: Float? = null
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (values.size < 2) return@Canvas

        val lo = minValue ?: values.min()
        val hi = maxValue ?: values.max()
        val range = (hi - lo).takeIf { it > 0.0001f } ?: 1f

        val stepX = size.width / (values.size - 1).coerceAtLeast(1)
        val points = values.mapIndexed { index, v ->
            val normalized = (v - lo) / range
            Offset(
                x = index * stepX,
                y = size.height - (normalized * size.height)
            )
        }

        val relleno = androidx.compose.ui.graphics.Path().apply {
            moveTo(points.first().x, size.height)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(points.last().x, size.height)
            close()
        }
        drawPath(
            path = relleno,
            brush = Brush.verticalGradient(listOf(lineColor.copy(alpha = 0.35f), lineColor.copy(alpha = 0.02f)))
        )

        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )
        }
    }
}

/** Barra horizontal simple para comparar dos valores (usada en luz y sonido). */
@Composable
fun ComparisonBar(
    label: String,
    value: Float,
    maxValue: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    val fraction = if (maxValue > 0f) (value / maxValue).coerceIn(0f, 1f) else 0f
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
    ) {
        drawRoundRect(
            color = color.copy(alpha = 0.2f),
            size = size
        )
        drawRoundRect(
            color = color,
            size = size.copy(width = size.width * fraction)
        )
    }
}
