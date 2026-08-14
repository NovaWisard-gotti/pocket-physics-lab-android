package com.kidslab.pocketphysics.data.repository

import com.kidslab.pocketphysics.domain.model.SoundAnalysisResult
import kotlinx.coroutines.flow.Flow

/**
 * Abstracción sobre el micrófono. Ninguna implementación de esta interfaz
 * debe guardar audio crudo en disco: solo se procesa en memoria y se
 * descarta, emitiendo un [SoundAnalysisResult] resumido.
 */
interface SoundRepository {

    /** true si el dispositivo tiene micrófono disponible. */
    fun isMicrophoneAvailable(): Boolean

    /**
     * Comienza a capturar y analizar audio mientras haya suscriptores.
     * Cada emisión es un análisis de un bloque corto de audio (amplitud,
     * frecuencia dominante y forma de onda simplificada). El audio en sí
     * nunca se persiste.
     */
    fun observeSoundAnalysis(): Flow<SoundAnalysisResult>
}
