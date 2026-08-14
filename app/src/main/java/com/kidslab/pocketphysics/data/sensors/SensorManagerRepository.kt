package com.kidslab.pocketphysics.data.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.kidslab.pocketphysics.data.repository.SensorRepository
import com.kidslab.pocketphysics.domain.model.LightSample
import com.kidslab.pocketphysics.domain.model.MotionSample
import com.kidslab.pocketphysics.domain.model.RotationSample
import com.kidslab.pocketphysics.domain.model.SensorType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Implementación real de [SensorRepository], apoyada en [SensorManager].
 * Usa SENSOR_DELAY_UI (algunas decenas de Hz), suficiente para dibujar
 * gráficos simples sin sobrecargar el hilo principal ni la batería.
 */
class SensorManagerRepository(context: Context) : SensorRepository {

    private val sensorManager: SensorManager =
        context.applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    override fun isSensorAvailable(type: SensorType): Boolean {
        val androidType = type.toAndroidSensorTypeOrNull() ?: return false
        return sensorManager.getDefaultSensor(androidType) != null
    }

    override fun observeAccelerometer(): Flow<MotionSample> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensor == null) {
            close()
            return@callbackFlow
        }
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                trySend(
                    MotionSample(
                        x = event.values[0],
                        y = event.values[1],
                        z = event.values[2],
                        timestampMs = System.currentTimeMillis()
                    )
                )
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        awaitClose { sensorManager.unregisterListener(listener) }
    }

    override fun observeGyroscope(): Flow<RotationSample> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if (sensor == null) {
            close()
            return@callbackFlow
        }
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                trySend(
                    RotationSample(
                        x = event.values[0],
                        y = event.values[1],
                        z = event.values[2],
                        timestampMs = System.currentTimeMillis()
                    )
                )
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        awaitClose { sensorManager.unregisterListener(listener) }
    }

    override fun observeLight(): Flow<LightSample> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (sensor == null) {
            close()
            return@callbackFlow
        }
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                trySend(LightSample(lux = event.values[0], timestampMs = System.currentTimeMillis()))
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        awaitClose { sensorManager.unregisterListener(listener) }
    }

    private fun SensorType.toAndroidSensorTypeOrNull(): Int? = when (this) {
        SensorType.ACCELEROMETER -> Sensor.TYPE_ACCELEROMETER
        SensorType.GYROSCOPE -> Sensor.TYPE_GYROSCOPE
        SensorType.LIGHT -> Sensor.TYPE_LIGHT
        SensorType.MICROPHONE -> null // el micrófono se gestiona aparte, con AudioRecord
    }
}
