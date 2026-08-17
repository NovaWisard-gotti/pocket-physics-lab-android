package com.kidslab.pocketphysics.ui.screens.challenges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.data.local.entity.Badge
import com.kidslab.pocketphysics.data.local.entity.Challenge
import com.kidslab.pocketphysics.ui.AppViewModelFactory
import com.kidslab.pocketphysics.ui.components.BouncyCard
import com.kidslab.pocketphysics.ui.theme.AppBackgroundGradient
import com.kidslab.pocketphysics.ui.theme.PhysicsGreen
import com.kidslab.pocketphysics.ui.theme.PhysicsYellow
import com.kidslab.pocketphysics.ui.theme.PhysicsYellowDeep

@Composable
fun ChallengesScreen(factory: AppViewModelFactory) {
    val viewModel: ChallengesViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(AppBackgroundGradient))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Text(
                text = "🏆 " + stringResource(R.string.desafios_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                "${state.userBadges.size} de ${state.badges.size} insignias ganadas ⭐",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item { Text("🎯 Desafíos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                items(state.challenges) { challenge -> ChallengeCard(challenge, state.isCompleted(challenge)) }

                item { Text("🏅 Insignias", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp)) }
                items(state.badges) { badge -> BadgeCard(badge, state.isBadgeEarned(badge)) }

                item {
                    Text(
                        text = stringResource(R.string.aviso_precision),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChallengeCard(challenge: Challenge, completado: Boolean) {
    BouncyCard(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        backgroundBrush = if (completado) Brush.horizontalGradient(listOf(PhysicsGreen, PhysicsGreen.copy(alpha = 0.7f))) else null,
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    challenge.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (completado) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    challenge.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (completado) androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(if (completado) "✅" else "⬜️", style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
private fun BadgeCard(badge: Badge, ganada: Boolean) {
    BouncyCard(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        backgroundBrush = if (ganada) Brush.horizontalGradient(listOf(PhysicsYellowDeep, PhysicsYellow)) else null,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    badge.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (ganada) androidx.compose.ui.graphics.Color(0xFF4A3300) else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    badge.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (ganada) androidx.compose.ui.graphics.Color(0xFF4A3300).copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                if (ganada) "🏅" else "🔒",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
