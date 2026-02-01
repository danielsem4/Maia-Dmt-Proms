package maia.dmt.proms.services

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
import maia.dmt.core.domain.sensors.storage.DeletionTracker
import maia.dmt.fallandshake.domain.usecase.DetectFallUseCase

class SensorService : Service(), KoinComponent {

    private val sensorRepository: SensorRepository by inject()
    private val detectFallUseCase: DetectFallUseCase by inject()
    private val deletionTracker: DeletionTracker by inject()
    private val tremorAnalyzer = TremorAnalysisUseCase()

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    private val accelDataQueue = LinkedBlockingQueue<Acceleration>(500)
    private val gyroDataQueue = LinkedBlockingQueue<Gyroscope>(500)
    private val lightDataQueue = LinkedBlockingQueue<Float>(500)
    private val stepDataQueue = LinkedBlockingQueue<Long>(500)

    private val frequencyBuffer = ArrayDeque<Acceleration>(128)

    private val tremorBuffer = ArrayDeque<Acceleration>(30)
    private val gyroBuffer = ArrayDeque<Gyroscope>(30)
    private val stepBuffer = ArrayDeque<Long>(30)
    private val lightBuffer = ArrayDeque<Float>(30)
    private val deletionBuffer = ArrayDeque<Int>(30)

    companion object {
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "Sensor_Service_Channel"
        const val UPDATE_INTERVAL = 500L // 2Hz processing cycle
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        startDataCollection()
        startProcessingLoop()
    }

    private fun startDataCollection() {
        serviceScope.launch {
            sensorRepository.getAcceleration().collect {
                addToQueue(accelDataQueue, it)
            }
        }
        serviceScope.launch {
            sensorRepository.getGyroscope().collect {
                addToQueue(gyroDataQueue, it)
            }
        }
        serviceScope.launch {
            sensorRepository.getStepCount().collect {
                addToQueue(stepDataQueue, it.steps)
            }
        }
        serviceScope.launch {
            sensorRepository.getLightLevel().collect {
                addToQueue(lightDataQueue, it.lux)
            }
        }
    }

    private fun <T> addToQueue(queue: LinkedBlockingQueue<T>, item: T) {
        if (queue.remainingCapacity() > 0) {
            queue.offer(item)
        } else {
            queue.poll()
            queue.offer(item)
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
        val rawAccel = mutableListOf<Acceleration>()
        val rawGyro = mutableListOf<Gyroscope>()
        val rawSteps = mutableListOf<Long>()
        val rawLight = mutableListOf<Float>()

        accelDataQueue.drainTo(rawAccel)
        gyroDataQueue.drainTo(rawGyro)
        stepDataQueue.drainTo(rawSteps)
        lightDataQueue.drainTo(rawLight)

        if (rawAccel.isNotEmpty()) {
            if (detectFallUseCase.execute(rawAccel)) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Fall Detected!", Toast.LENGTH_LONG).show()
                }
            }

            for (point in rawAccel) {
                if (frequencyBuffer.size >= 128) frequencyBuffer.removeFirst()
                frequencyBuffer.add(point)
            }

            val avgAccelX = rawAccel.map { it.x }.average().toFloat()
            val avgAccelY = rawAccel.map { it.y }.average().toFloat()
            val avgAccelZ = rawAccel.map { it.z }.average().toFloat()
            val lastTimestamp = rawAccel.last().timestamp
            val averagedAccel = Acceleration(avgAccelX, avgAccelY, avgAccelZ, lastTimestamp)

            val avgGyroX = if (rawGyro.isNotEmpty()) rawGyro.map { it.x }.average().toFloat() else 0f
            val avgGyroY = if (rawGyro.isNotEmpty()) rawGyro.map { it.y }.average().toFloat() else 0f
            val avgGyroZ = if (rawGyro.isNotEmpty()) rawGyro.map { it.z }.average().toFloat() else 0f
            val averagedGyro = Gyroscope(avgGyroX, avgGyroY, avgGyroZ, lastTimestamp)

            val avgLight = if (rawLight.isNotEmpty()) rawLight.average().toFloat() else 0f
            val currentSteps = if (rawSteps.isNotEmpty()) rawSteps.last() else 0L
            val currentDeletions = deletionTracker.getDeleteCount()

            addToDeque(tremorBuffer, averagedAccel, 30)
            addToDeque(gyroBuffer, averagedGyro, 30)
            addToDeque(lightBuffer, avgLight, 30)
            addToDeque(stepBuffer, currentSteps, 30)
            addToDeque(deletionBuffer, currentDeletions, 30)

            if (tremorBuffer.size >= 30 && frequencyBuffer.size >= 64) {

                val resultData = tremorAnalyzer.analyze(
                    accelBuffer = tremorBuffer.toList(),
                    gyroBuffer = gyroBuffer.toList(),
                    stepBuffer = stepBuffer.toList(),
                    lightBuffer = lightBuffer.toList(),
                    deletionBuffer = deletionBuffer.toList(),
                    rawAccelList = frequencyBuffer.toList()
                )

                if (resultData != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Parkinson's Tremor Detected!", Toast.LENGTH_LONG).show()
                    }
                    (sensorRepository as? AndroidSensorRepository)?.uploadTremorData(resultData)

                    // Clear buffers to reset detection cycle
                    tremorBuffer.clear()
                    frequencyBuffer.clear()
                }
            }
        }
    }

    private fun <T> addToDeque(deque: ArrayDeque<T>, item: T, maxSize: Int) {
        if (deque.size >= maxSize) deque.removeFirst()
        deque.add(item)
    }

    private fun startForegroundService() {
        val channel = NotificationChannel(CHANNEL_ID, "Health Sensor", NotificationManager.IMPORTANCE_LOW)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Health Sensors Active")
            .setContentText("Monitoring...")
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setOngoing(true)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}