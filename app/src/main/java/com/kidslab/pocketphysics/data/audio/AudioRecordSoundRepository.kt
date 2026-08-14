package com.kidslab.pocketphysics.data.audio

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import android.Manifest
import com.kidslab.pocketphysics.data.repository.SoundRepository
import com.kidslab.pocketphysics.domain.model.SoundAnalysisResult
import com.kidslab.pocketphysics.domain.usecase.AudioAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive

/**
 * Implementación real del laboratorio de sonido usando [AudioRecord].
 *
 * Reglas de privacidad que esta clase respeta siempre:
 * - Nunca escribe el PCM capturado a un archivo.
 * - Cada bloque leído se analiza y se descarta inmediatamente (el buffer
 *   se sobrescribe en la siguiente iteración del bucle).
 * - No usa Internet ni SpeechRecognizer.
 * - Solo se activa mientras el laboratorio de sonido está en pantalla.
 */
class AudioRecordSoundRepository(private val context: Context) : SoundRepository {

    companion object {
        private const val SAMPLE_RATE = 16_000 // suficiente para voz y tonos simples
        private const val ANALYSIS_WINDOW_SAMPLES = 2048
    }

    override fun isMicrophoneAvailable(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
    }

    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED
    }

    override fun observeSoundAnalysis(): Flow<SoundAnalysisResult> = callbackFlow {
        if (!hasPermission()) {
            close(SecurityException("Permiso RECORD_AUDIO no concedido"))
            return@callbackFlow
        }

        val minBufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ).coerceAtLeast(ANALYSIS_WINDOW_SAMPLES * 2)

        val audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBufferSize
        )

        if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
            audioRecord.release()
            close(IllegalStateException("No se pudo inicializar el micrófono"))
            return@callbackFlow
        }

        // Buffer reutilizado en cada vuelta: nunca se acumula ni se persiste.
        val buffer = ShortArray(ANALYSIS_WINDOW_SAMPLES)

        audioRecord.startRecording()
        try {
            while (isActive) {
                val read = audioRecord.read(buffer, 0, buffer.size)
                if (read > 0) {
                    val chunk = if (read == buffer.size) buffer else buffer.copyOf(read)
                    val result = AudioAnalyzer.analyze(chunk, SAMPLE_RATE)
                    trySend(result)
                }
            }
        } finally {
            audioRecord.stop()
            audioRecord.release()
        }

        awaitClose {
            // La liberación ya ocurrió en el finally de arriba; esto cubre
            // cancelaciones que interrumpen el bucle antes del finally.
            runCatching { audioRecord.release() }
        }
    }.flowOn(Dispatchers.IO)
}
