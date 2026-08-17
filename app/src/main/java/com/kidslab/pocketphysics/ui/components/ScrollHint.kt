package com.kidslab.pocketphysics.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Pista animada de "hay más para ver deslizando". Se usa sobre listas
 * horizontales (como el selector de experimentos) que a simple vista
 * parecen tener un solo elemento visible y los niños no notan que se
 * puede deslizar. Aparece solo mientras de verdad se puede seguir
 * deslizando hacia la derecha, y desaparece sola al llegar al final.
 */
@Composable
fun HorizontalScrollHint(
    listState: LazyListState,
    modifier: Modifier = Modifier,
    texto: String = "Desliza para ver más"
) {
    val puedeSeguir by remember {
        derivedStateOf {
            listState.canScrollForward || listState.layoutInfo.totalItemsCount > listState.layoutInfo.visibleItemsInfo.size
        }
    }

    AnimatedVisibility(visible = puedeSeguir, enter = fadeIn(), exit = fadeOut(), modifier = modifier) {
        val infiniteTransition = rememberInfiniteTransition(label = "scrollHintBounce")
        val offsetX by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 8f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scrollHintOffset"
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
        ) {
            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(x = offsetX.dp)
            )
        }
    }
}

/**
 * Igual que [HorizontalScrollHint] pero para listas verticales: avisa que
 * hay más tarjetas si se sigue bajando.
 */
@Composable
fun VerticalScrollHint(
    listState: LazyListState,
    modifier: Modifier = Modifier,
    texto: String = "Desliza hacia abajo para ver más"
) {
    val puedeSeguir by remember {
        derivedStateOf { listState.canScrollForward }
    }

    AnimatedVisibility(visible = puedeSeguir, enter = fadeIn(), exit = fadeOut(), modifier = modifier) {
        val infiniteTransition = rememberInfiniteTransition(label = "verticalScrollHintBounce")
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 6f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "verticalScrollHintOffset"
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(y = offsetY.dp)
            )
        }
    }
}
