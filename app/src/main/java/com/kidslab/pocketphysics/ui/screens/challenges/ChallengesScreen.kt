package com.kidslab.pocketphysics.ui.screens.challenges

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.data.local.entity.Badge
import com.kidslab.pocketphysics.data.local.entity.Challenge
import com.kidslab.pocketphysics.ui.AppViewModelFactory
import com.kidslab.pocketphysics.ui.theme.PhysicsGreen
import com.kidslab.pocketphysics.ui.theme.PhysicsYellow

@Composable
fun ChallengesScreen(factory: AppViewModelFactory) {
    val viewModel: ChallengesViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(stringResource(R.string.desafios_titulo), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(
            "${state.userBadges.size} de ${state.badges.size} insignias ganadas",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { Text("Desafíos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
            items(state.challenges) { challenge -> ChallengeCard(challenge, state.isCompleted(challenge)) }

            item { Text("Insignias", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 16.dp)) }
            items(state.badges) { badge -> BadgeCard(badge, state.isBadgeEarned(badge)) }

            item {
                Text(
                    text = stringResource(R.string.aviso_precision),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun ChallengeCard(challenge: Challenge, completado: Boolean) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(challenge.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(challenge.descripcion, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
            }
            Text(if (completado) "✅" else "⬜️", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
private fun BadgeCard(badge: Badge, ganada: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (ganada) PhysicsYellow.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(badge.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(badge.descripcion, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
            }
            Text(
                if (ganada) "🏅" else "🔒",
                style = MaterialTheme.typography.headlineMedium,
                color = if (ganada) PhysicsGreen else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
