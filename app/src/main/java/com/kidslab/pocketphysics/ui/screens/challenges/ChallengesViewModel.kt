package com.kidslab.pocketphysics.ui.screens.challenges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidslab.pocketphysics.data.local.entity.Badge
import com.kidslab.pocketphysics.data.local.entity.Challenge
import com.kidslab.pocketphysics.data.local.entity.ChallengeCompletion
import com.kidslab.pocketphysics.data.local.entity.UserBadge
import com.kidslab.pocketphysics.data.repository.ChallengeRepository
import com.kidslab.pocketphysics.data.repository.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class ChallengesUiState(
    val challenges: List<Challenge> = emptyList(),
    val completions: List<ChallengeCompletion> = emptyList(),
    val badges: List<Badge> = emptyList(),
    val userBadges: List<UserBadge> = emptyList(),
    val cargando: Boolean = true
) {
    fun isCompleted(challenge: Challenge) = completions.any { it.challengeKey == challenge.challengeKey }
    fun isBadgeEarned(badge: Badge) = userBadges.any { it.badgeKey == badge.badgeKey }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ChallengesViewModel(
    private val profileRepository: ProfileRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengesUiState())
    val uiState: StateFlow<ChallengesUiState> = _uiState.asStateFlow()

    init {
        val profileIdFlow = profileRepository.observeProfile()

        val completionsFlow = profileIdFlow.flatMapLatest { profile ->
            profile?.let { challengeRepository.observeCompletions(it.id) } ?: flowOf(emptyList())
        }
        val userBadgesFlow = profileIdFlow.flatMapLatest { profile ->
            profile?.let { challengeRepository.observeUserBadges(it.id) } ?: flowOf(emptyList())
        }

        combine(
            challengeRepository.observeChallenges(),
            completionsFlow,
            challengeRepository.observeBadges(),
            userBadgesFlow
        ) { challenges, completions, badges, userBadges ->
            ChallengesUiState(
                challenges = challenges,
                completions = completions,
                badges = badges,
                userBadges = userBadges,
                cargando = false
            )
        }.onEach { _uiState.value = it }.launchIn(viewModelScope)
    }
}
