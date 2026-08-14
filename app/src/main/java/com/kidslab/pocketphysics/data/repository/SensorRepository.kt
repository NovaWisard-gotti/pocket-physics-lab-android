package com.kidslab.pocketphysics.data.repository

import com.kidslab.pocketphysics.domain.model.LightSample
import com.kidslab.pocketphysics.domain.model.MotionSample
import com.kidslab.pocketphysics.domain.model.RotationSample
import com.kidslab.pocketphysics.domain.model.SensorType
import kotlinx.coroutines.flow.Flow

/**
 * Abstracción sobre el hardware de sensores (acelerómetro, giroscopio, luz).
 *
 * El ViewModel nunca habla directamente con [android.hardware.SensorManager]:
 * siempre pasa por esta interfaz, lo que permite reemplazarla por una
 * implementación falsa (fake) en los tests unitarios, sin necesitar
 * hardware real ni Robolectric shadows complejos.
 */
interface SensorRepository {

    /** Indica si el sensor pedido existe en este dispositivo. */
    fun isSensorAvailable(type: SensorType): Boolean

    /** Emite lecturas del acelerómetro mientras haya suscriptores. */
    fun observeAccelerometer(): Flow<MotionSample>

    /** Emite lecturas del giroscopio mientras haya suscriptores. */
    fun observeGyroscope(): Flow<RotationSample>

    /** Emite lecturas del sensor de luz mientras haya suscriptores (si existe). */
    fun observeLight(): Flow<LightSample>
}
