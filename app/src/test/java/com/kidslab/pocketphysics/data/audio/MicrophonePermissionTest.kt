package com.kidslab.pocketphysics.data.audio

import android.Manifest
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

/**
 * Prueba el caso de "micrófono denegado": cuando el permiso RECORD_AUDIO
 * no ha sido concedido, el laboratorio de sonido NUNCA debe intentar
 * capturar audio, y debe fallar de forma controlada en vez de crashear.
 */
@RunWith(RobolectricTestRunner::class)
class MicrophonePermissionTest {

    @Test
    fun `sin permiso RECORD_AUDIO el flujo de analisis se cierra con error controlado`() = runTest {
        val application = ApplicationProvider.getApplicationContext<android.app.Application>()
        shadowOf(application).denyPermissions(Manifest.permission.RECORD_AUDIO)

        val repository = AudioRecordSoundRepository(application)

        var errorRecibido: Throwable? = null
        val resultados = repository.observeSoundAnalysis()
            .catch { e -> errorRecibido = e }
            .toList()

        assertThat(resultados).isEmpty()
        assertThat(errorRecibido).isInstanceOf(SecurityException::class.java)
    }

    @Test
    fun `FakeSoundRepository simula microfono no disponible sin hardware`() = runTest {
        val fake = FakeSoundRepository(available = false)
        assertThat(fake.isMicrophoneAvailable()).isFalse()
    }
}
