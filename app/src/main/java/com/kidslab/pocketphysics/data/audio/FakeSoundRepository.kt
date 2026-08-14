package com.kidslab.pocketphysics.data.audio

import com.kidslab.pocketphysics.data.repository.SoundRepository
import com.kidslab.pocketphysics.domain.model.SoundAnalysisResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

/** Implementación falsa del laboratorio de sonido, usada en tests unitarios. */
class FakeSoundRepository(
    private val available: Boolean = true,
    private val results: List<SoundAnalysisResult> = emptyList()
) : SoundRepository {
    override fun isMicrophoneAvailable(): Boolean = available
    override fun observeSoundAnalysis(): Flow<SoundAnalysisResult> = results.asFlow()
}
