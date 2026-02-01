package maia.dmt.core.data.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import maia.dmt.core.data.dto.sensors.SensorsDataDto
import maia.dmt.core.data.dto.sensors.SensorsDataServerRequest
import maia.dmt.core.data.sensors.util.SensorMathUtils
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.sensors.SensorsService
import maia.dmt.core.domain.sensors.model.*
import maia.dmt.core.domain.sensors.repository.SensorRepository
import maia.dmt.core.domain.sensors.storage.DeletionTracker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AndroidSensorRepository(
    private val context: Context,
    private val sensorsService: SensorsService,
    private val sessionStorage: SessionStorage,
    private val deletionTracker: DeletionTracker
) : SensorRepository {

    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    override fun getAcceleration(): Flow<Acceleration> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val gravity = FloatArray(3)
        val linearAcceleration = FloatArray(3)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    val (x, y, z) = SensorMathUtils.processAccelerometer(
                        event.values[0],
                        event.values[1],
                        event.values[2],
                        gravity,
                        linearAcceleration
                    )
                    trySend(Acceleration(x, y, z, System.currentTimeMillis()))
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (sensor != null) {
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
        } else {
            close()
        }
        awaitClose { sensorManager.unregisterListener(listener) }
    }

    override fun getGyroscope(): Flow<Gyroscope> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    // Apply Math Utils Process (Normalizing if needed)
                    val (x, y, z) = SensorMathUtils.processGyroscope(
                        event.values[0], event.values[1], event.values[2]
                    )
                    trySend(Gyroscope(x, y, z, System.currentTimeMillis()))
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        if (sensor != null) sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
        else close()
        awaitClose { sensorManager.unregisterListener(listener) }
    }

    override fun getStepCount(): Flow<StepCount> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) trySend(StepCount(event.values[0].toLong()))
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        if (sensor != null) sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        else close()
        awaitClose { sensorManager.unregisterListener(listener) }
    }
    override fun getLightLevel(): Flow<LightLevel> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) trySend(LightLevel(event.values[0]))
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        if (sensor != null) sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        else close()
        awaitClose { sensorManager.unregisterListener(listener) }
    }

    override fun startListening() {}
    override fun stopListening() {}

    // --- UPLOAD LOGIC ---
    suspend fun uploadTremorData(domainData: SensorsData) {
        try {
            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            val clinicId = authInfo?.user?.clinicId
            val uid = authInfo?.user?.id

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault())
            val formattedDate = dateFormat.format(Date())

            val dto = SensorsDataDto(
                avgFrequency = domainData.avgFrequency,
                stdDevX = domainData.stdDevX,
                stdDevZ = domainData.stdDevZ,
                threshold = domainData.threshold,
                rangeX = domainData.rangeX,
                rangeZ = domainData.rangeZ,
                rangeGyroX = domainData.rangeGyroX,
                rangeGyroZ = domainData.rangeGyroZ,
                steps = domainData.steps,
                stdDevSteps = domainData.stdDevSteps,
                stdDevDeletions = domainData.stdDevDeletions,
                rangeDeletions = domainData.rangeDeletions
            )

            val request = SensorsDataServerRequest(
                patientId = uid ?: 0,
                clinicId = clinicId ?: 0,
                uploadDate = formattedDate,
                data = dto
            )

            sensorsService.uploadSensorsAggResults(request)
            deletionTracker.resetDeleteCount()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}