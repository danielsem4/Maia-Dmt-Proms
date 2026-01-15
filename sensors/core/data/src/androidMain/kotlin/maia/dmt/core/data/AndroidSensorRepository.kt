package maia.dmt.core.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import maia.dmt.core.domain.model.Acceleration
import maia.dmt.core.domain.model.Gyroscope
import maia.dmt.core.domain.model.LightLevel
import maia.dmt.core.domain.model.StepCount
import maia.dmt.core.domain.repository.SensorRepository

class AndroidSensorRepository(
    private val context: Context
) : SensorRepository {

    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    /**
     * Creates a Flow that registers a listener on collection and unregisters on cancellation.
     */
    private fun getSensorFlow(sensorType: Int): Flow<SensorEvent> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(sensorType)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                // event.values is reused by the system, so we must copy logic here if we were buffering
                // raw events, but since we map to domain models immediately downstream, passing 'event' is safe.
                if (event != null) {
                    trySend(event)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // No-op
            }
        }

        if (sensor != null) {
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
        } else {
            // Handle devices missing the sensor (optional: log or close)
            close()
        }

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    override fun getAcceleration(): Flow<Acceleration> {
        return getSensorFlow(Sensor.TYPE_ACCELEROMETER)
            .map { event: SensorEvent ->
                Acceleration(
                    x = event.values[0],
                    y = event.values[1],
                    z = event.values[2],
                    timestamp = System.currentTimeMillis()
                )
            }
    }

    override fun getGyroscope(): Flow<Gyroscope> {
        return getSensorFlow(Sensor.TYPE_GYROSCOPE)
            .map { event: SensorEvent ->
                Gyroscope(
                    x = event.values[0],
                    y = event.values[1],
                    z = event.values[2],
                    timestamp = System.currentTimeMillis()
                )
            }
    }

    override fun getLightLevel(): Flow<LightLevel> {
        return getSensorFlow(Sensor.TYPE_LIGHT)
            .map { event: SensorEvent ->
                LightLevel(lux = event.values[0])
            }
    }

    override fun getStepCount(): Flow<StepCount> {
        return getSensorFlow(Sensor.TYPE_STEP_COUNTER)
            .map { event: SensorEvent ->
                StepCount(steps = event.values[0].toLong())
            }
    }

    override fun startListening() {
        // No-op in Flow architecture (listening starts on collect)
    }

    override fun stopListening() {
        // No-op (listening stops on scope cancellation)
    }
}