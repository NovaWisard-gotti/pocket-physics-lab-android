package com.kidslab.pocketphysics.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val AVATAR_EMOJI = mapOf(
    "atomo" to "⚛️", "cohete" to "🚀", "lupa" to "🔍",
    "iman" to "🧲", "prisma" to "🔺", "engranaje" to "⚙️"
)

private val AVATAR_GRADIENTS = listOf(
    listOf(Color(0xFF3E7BFA), Color(0xFF3DDBFF)),
    listOf(Color(0xFFFF9E3D), Color(0xFFFF6B6B)),
    listOf(Color(0xFF9D6FFF), Color(0xFFFF6FD8)),
    listOf(Color(0xFFFFD34D), Color(0xFF3DD68C))
)

private fun gradientFor(avatarKey: String): List<Color> {
    val index = (avatarKey.hashCode().let { if (it < 0) -it else it }) % AVATAR_GRADIENTS.size
    return AVATAR_GRADIENTS[index]
}

/** Burbuja circular con degradado y el emoji del avatar elegido por el científico. */
@Composable
fun ProfileAvatar(
    avatarKey: String,
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
    selected: Boolean = false
) {
    Box(
        modifier = modifier
            .size(size)
            .background(Brush.linearGradient(gradientFor(avatarKey)), CircleShape)
            .then(
                if (selected) Modifier.border(3.dp, Color.White, CircleShape) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = AVATAR_EMOJI[avatarKey] ?: "⚛️",
            style = TextStyle(fontSize = (size.value * 0.45f).sp)
        )
    }
}
