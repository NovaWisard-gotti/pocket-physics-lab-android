package com.kidslab.pocketphysics.data.sensors

import com.kidslab.pocketphysics.data.repository.SensorRepository
import com.kidslab.pocketphysics.domain.model.LightSample
import com.kidslab.pocketphysics.domain.model.MotionSample
import com.kidslab.pocketphysics.domain.model.RotationSample
import com.kidslab.pocketphysics.domain.model.SensorType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

/**
 * Implementación falsa de [SensorRepository] usada en pruebas unitarias y,
 * opcionalmente, como respaldo si un sensor no existe en el dispositivo real.
 * No depende de [android.hardware.SensorManager] ni de hardware físico.
 */
class FakeSensorRepository(
    private val availability: Map<SensorType, Boolean> = SensorType.entries.associateWith { true },
    private val accelerometerData: List<MotionSample> = emptyList(),
    private val gyroscopeData: List<RotationSample> = emptyList(),
    private val lightData: List<LightSample> = emptyList()
) : SensorRepository {

    override fun isSensorAvailable(type: SensorType): Boolean = availability[type] ?: false

    override fun observeAccelerometer(): Flow<MotionSample> = accelerometerData.asFlow()

    override fun observeGyroscope(): Flow<RotationSample> = gyroscopeData.asFlow()

    override fun observeLight(): Flow<LightSample> = lightData.asFlow()
}
