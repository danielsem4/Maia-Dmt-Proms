package maia.dmt.proms.services

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.ArrayDeque
import java.util.concurrent.LinkedBlockingQueue

import maia.dmt.core.data.sensors.AndroidSensorRepository
import maia.dmt.core.data.sensors.analysis.TremorAnalysisUseCase
import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.repository.SensorRepository
import maia.dmt.fallandshake.domain.usecase.DetectFallUseCase

class SensorService : Service(), KoinComponent {

    private val sensorRepository: SensorRepository by inject()
    private val detectFallUseCase: DetectFallUseCase by inject()
    private val tremorAnalyzer = TremorAnalysisUseCase()

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)
    private val accelDataQueue = LinkedBlockingQueue<Acceleration>(500)
    private val tremorBuffer = ArrayDeque<Acceleration>(500)
    private val gyroBuffer = ArrayDeque<Gyroscope>(500)
    private val stepBuffer = ArrayDeque<Long>(500)

    companion object {
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "Sensor_Service_Channel"
        const val UPDATE_INTERVAL = 500L
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        startDataCollection()
        startProcessingLoop()
    }

    private fun startDataCollection() {
        serviceScope.launch {
            sensorRepository.getAcceleration().collect { data ->
                if (accelDataQueue.remainingCapacity() > 0) {
                    accelDataQueue.offer(data)
                } else {
                    accelDataQueue.poll()
                    accelDataQueue.offer(data)
                }
            }
        }
        serviceScope.launch {
            sensorRepository.getGyroscope().collect { data ->
                if (gyroBuffer.size >= 500) gyroBuffer.removeFirst()
                gyroBuffer.add(data)
            }
        }
        serviceScope.launch {
            sensorRepository.getStepCount().collect { data ->
                if (stepBuffer.size >= 500) stepBuffer.removeFirst()
                stepBuffer.add(data.steps)
            }
        }
    }

    private fun startProcessingLoop() {
        serviceScope.launch {
            while (isActive) {
                delay(UPDATE_INTERVAL)
                processSensorData()
            }
        }
    }

    private suspend fun processSensorData() {
        val newRawData = mutableListOf<Acceleration>()
        accelDataQueue.drainTo(newRawData)

        if (newRawData.isNotEmpty()) {

            val fallDetected = detectFallUseCase.execute(newRawData)
            if (fallDetected) {
                withContext(Dispatchers.Main) {
//                    Toast.makeText(applicationContext, "Fall Detected!", Toast.LENGTH_LONG).show()
                }
            }

            for (item in newRawData) {
                if (tremorBuffer.size >= 500) tremorBuffer.removeFirst()
                tremorBuffer.add(item)
            }

            if (tremorBuffer.size >= 240) {

                val resultData = tremorAnalyzer.analyze(
                    accelBuffer = tremorBuffer.toList(),
                    gyroBuffer = gyroBuffer.toList(),
                    stepBuffer = stepBuffer.toList(),
                    timestamp = System.currentTimeMillis()
                )

                if (resultData != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Tremor Detected! Uploading...", Toast.LENGTH_SHORT).show()
                    }

                    (sensorRepository as? AndroidSensorRepository)?.uploadTremorData(resultData)

                    tremorBuffer.clear()
                }
            }
        }
    }

    private fun startForegroundService() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Health Sensor Monitoring",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Health Sensors Active")
            .setContentText("Monitoring movement patterns...")
            .setSmallIcon(R.drawable.ic_popup_sync)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}