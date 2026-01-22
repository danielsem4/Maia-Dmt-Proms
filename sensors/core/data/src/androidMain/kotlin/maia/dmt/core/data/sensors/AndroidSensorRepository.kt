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
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.sensors.SensorsService
import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.LightLevel
import maia.dmt.core.domain.sensors.model.SensorsData
import maia.dmt.core.domain.sensors.model.StepCount
import maia.dmt.core.domain.sensors.repository.SensorRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AndroidSensorRepository(
    private val context: Context,
    private val sensorsService: SensorsService,
    private val sessionStorage: SessionStorage,

) : SensorRepository {

    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private fun getSensorFlow(sensorType: Int): Flow<SensorEvent> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(sensorType)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) trySend(event)
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

    override fun getAcceleration(): Flow<Acceleration> {
        return getSensorFlow(Sensor.TYPE_ACCELEROMETER).map { event ->
            Acceleration(event.values[0], event.values[1], event.values[2], System.currentTimeMillis())
        }
    }

    override fun getGyroscope(): Flow<Gyroscope> {
        return getSensorFlow(Sensor.TYPE_GYROSCOPE).map { event ->
            Gyroscope(event.values[0], event.values[1], event.values[2], System.currentTimeMillis())
        }
    }

    override fun getLightLevel(): Flow<LightLevel> {
        return getSensorFlow(Sensor.TYPE_LIGHT).map { event ->
            LightLevel(event.values[0])
        }
    }

    override fun getStepCount(): Flow<StepCount> {
        return getSensorFlow(Sensor.TYPE_STEP_COUNTER).map { event ->
            StepCount(event.values[0].toLong())
        }
    }

    override fun startListening() { }
    override fun stopListening() { /* No-op */ }

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
                stdDevDeletions = 0f,
                rangeDeletions = 0f
            )

            val request = SensorsDataServerRequest(
                patientId = uid!!,
                clinicId = clinicId!!,
                uploadDate = formattedDate,
                data = dto
            )

            val upload = sensorsService.uploadSensorsAggResults(request)
            println(upload)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}