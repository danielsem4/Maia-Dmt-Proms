package maia.dmt.proms.services

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
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
import maia.dmt.core.domain.sensors.model.AnomalyEvent
import maia.dmt.core.domain.sensors.model.AnomalyEventType
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.repository.SensorRepository
import maia.dmt.core.domain.sensors.storage.DeletionTracker
import maia.dmt.fallandshake.domain.usecase.DetectFallUseCase
import maia.dmt.fallandshake.domain.usecase.DetectNearFallUseCase
import maia.dmt.fallandshake.domain.usecase.FallDetectionResult
import maia.dmt.fallandshake.domain.usecase.NearFallDetectionResult
import maia.dmt.onoffstate.data.OnOffStateRepository
import maia.dmt.onoffstate.domain.model.MedicationState
import maia.dmt.onoffstate.domain.model.PatientBaseline
import maia.dmt.onoffstate.domain.usecase.ClassifyOnOffStateUseCase
import maia.dmt.onoffstate.domain.usecase.ComputeActivityMetricsUseCase

class SensorService : Service(), KoinComponent {

    private val sensorRepository: SensorRepository by inject()
    private val detectFallUseCase: DetectFallUseCase by inject()
    private val detectNearFallUseCase: DetectNearFallUseCase by inject()
    private val deletionTracker: DeletionTracker by inject()
    private val tremorAnalyzer = TremorAnalysisUseCase()
    private lateinit var eventCacheManager: EventCacheManager

    // ON/OFF state detection
    private val computeActivityMetrics: ComputeActivityMetricsUseCase by inject()
    private val classifyOnOffState: ClassifyOnOffStateUseCase by inject()
    private val onOffStateRepository: OnOffStateRepository by inject()

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    // Raw sensor data queues (filled by sensor callbacks)
    private val accelDataQueue = LinkedBlockingQueue<Acceleration>(500)
    private val rawAccelDataQueue = LinkedBlockingQueue<Acceleration>(500)
    private val gyroDataQueue = LinkedBlockingQueue<Gyroscope>(500)
    private val lightDataQueue = LinkedBlockingQueue<Float>(500)
    private val stepDataQueue = LinkedBlockingQueue<Long>(500)

    // FFT frequency buffer for tremor analysis
    private val frequencyBuffer = ArrayDeque<Acceleration>(256)

    // Averaged analysis buffers for tremor stats
    private val tremorBuffer = ArrayDeque<Acceleration>(30)
    private val gyroBuffer = ArrayDeque<Gyroscope>(30)
    private val stepBuffer = ArrayDeque<Long>(30)
    private val lightBuffer = ArrayDeque<Float>(30)
    private val deletionBuffer = ArrayDeque<Int>(30)

    // Ring buffers for raw data snapshots around events (~7s at 50Hz)
    private val rawAccelRingBuffer = ArrayDeque<Acceleration>(RAW_BUFFER_CAPACITY)
    private val rawGyroRingBuffer = ArrayDeque<Gyroscope>(RAW_BUFFER_CAPACITY)

    // Post-event capture state (for fall/near-fall, capture 2s of data after event)
    private var postEventCaptureRemaining = 0
    private var pendingEventType: AnomalyEventType? = null
    private var pendingAggregatedStats: maia.dmt.core.domain.sensors.model.SensorsData? = null
    private var preEventAccelSnapshot: List<Acceleration> = emptyList()
    private var preEventGyroSnapshot: List<Gyroscope> = emptyList()
    private val postEventAccel = mutableListOf<Acceleration>()
    private val postEventGyro = mutableListOf<Gyroscope>()

    // Cooldown to prevent duplicate events
    private var lastEventTimestamp = 0L

    // ON/OFF state detection buffers (5 min sliding window at 2Hz)
    private val onOffAccelBuffer = ArrayDeque<Acceleration>(ON_OFF_WINDOW_SIZE)
    private val onOffGyroBuffer = ArrayDeque<Gyroscope>(ON_OFF_WINDOW_SIZE)
    private val onOffStepBuffer = ArrayDeque<Long>(ON_OFF_WINDOW_SIZE)
    private var cachedBaseline: PatientBaseline? = null
    private var lastOnOffState: MedicationState? = null
    private var lastOnOffEvalTimestamp = 0L

    companion object {
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "Sensor_Service_Channel"
        const val UPDATE_INTERVAL = 500L // 2Hz processing cycle
        const val RAW_BUFFER_CAPACITY = 400
        const val POST_EVENT_CYCLES = 4 // ~2s of post-event capture
        const val EVENT_COOLDOWN_MS = 30_000L
        const val RETRY_INTERVAL = 60_000L // 60s between upload retries
        const val RESTART_REQUEST_CODE = 1002
        const val ON_OFF_WINDOW_SIZE = 600        // 5 min at 2Hz
        const val ON_OFF_MIN_BUFFER = 300          // 2.5 min minimum data
        const val ON_OFF_EVAL_INTERVAL = 60_000L   // evaluate every 60s
    }

    override fun onCreate() {
        super.onCreate()
        eventCacheManager = EventCacheManager(applicationContext)
        startForegroundService()
        startDataCollection()
        startProcessingLoop()
        startRetryLoop()
        loadCachedBaseline()
    }

    private fun startDataCollection() {
        serviceScope.launch {
            sensorRepository.getAcceleration().collect {
                addToQueue(accelDataQueue, it)
            }
        }
        serviceScope.launch {
            sensorRepository.getRawAcceleration().collect {
                addToQueue(rawAccelDataQueue, it)
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

    private fun startRetryLoop() {
        serviceScope.launch {
            while (isActive) {
                delay(RETRY_INTERVAL)
                retryPendingUploads()
            }
        }
    }

    private suspend fun retryPendingUploads() {
        val pending = eventCacheManager.getPendingEvents()
        if (pending.isEmpty()) return
        Log.d("SensorEvent", "Retrying ${pending.size} pending event uploads...")
        val repo = sensorRepository as? AndroidSensorRepository ?: return
        for ((file, request) in pending) {
            val success = repo.uploadRequest(request)
            if (success) {
                eventCacheManager.removeEvent(file)
                Log.d("SensorEvent", "Retry upload succeeded: ${file.name}")
            } else {
                Log.w("SensorEvent", "Retry upload failed: ${file.name}, will try again later")
                break // Stop on first failure — server likely down
            }
        }
    }

    private suspend fun processSensorData() {
        val rawAccel = mutableListOf<Acceleration>()
        val rawAccelRaw = mutableListOf<Acceleration>()
        val rawGyro = mutableListOf<Gyroscope>()
        val rawSteps = mutableListOf<Long>()
        val rawLight = mutableListOf<Float>()

        accelDataQueue.drainTo(rawAccel)
        rawAccelDataQueue.drainTo(rawAccelRaw)
        gyroDataQueue.drainTo(rawGyro)
        stepDataQueue.drainTo(rawSteps)
        lightDataQueue.drainTo(rawLight)

        if (rawAccel.isEmpty()) return

        // 1. Append raw data to ring buffers (sliding window for event snapshots)
        for (point in rawAccel) {
            if (rawAccelRingBuffer.size >= RAW_BUFFER_CAPACITY) rawAccelRingBuffer.removeFirst()
            rawAccelRingBuffer.add(point)
        }
        for (point in rawGyro) {
            if (rawGyroRingBuffer.size >= RAW_BUFFER_CAPACITY) rawGyroRingBuffer.removeFirst()
            rawGyroRingBuffer.add(point)
        }

        // 2. If we're in post-event capture mode, accumulate and check if done
        if (postEventCaptureRemaining > 0) {
            postEventAccel.addAll(rawAccel)
            postEventGyro.addAll(rawGyro)
            postEventCaptureRemaining--

            if (postEventCaptureRemaining == 0) {
                uploadPendingEvent()
            }
            return // Don't run detectors during post-event capture
        }

        // 3. Check cooldown
        val now = System.currentTimeMillis()
        val inCooldown = (now - lastEventTimestamp) < EVENT_COOLDOWN_MS

        // 4. Run fall detection (priority 1)
        if (!inCooldown) {
            val fallResult = detectFallUseCase.evaluate(rawAccel, rawGyro, rawAccelRaw)
            if (fallResult is FallDetectionResult.FallDetected) {
                Log.d("SensorEvent", "FALL DETECTED!")
                startPostEventCapture(AnomalyEventType.FALL, null)
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Fall Detected!", Toast.LENGTH_LONG).show()
                }
                return
            }
        }

        // 5. Run near-fall detection (priority 2)
        if (!inCooldown) {
            val nearFallResult = detectNearFallUseCase.evaluate(rawAccel, rawGyro)
            if (nearFallResult is NearFallDetectionResult.NearFallDetected) {
                Log.d("SensorEvent", "NEAR-FALL DETECTED!")
                startPostEventCapture(AnomalyEventType.NEAR_FALL, null)
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Near-Fall (Stumble) Detected!", Toast.LENGTH_LONG).show()
                }
                return
            }
        }

        // 6. Feed data into tremor analysis buffers (existing logic)
        for (point in rawAccel) {
            if (frequencyBuffer.size >= 256) frequencyBuffer.removeFirst()
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

        // Feed ON/OFF activity window
        addToDeque(onOffAccelBuffer, averagedAccel, ON_OFF_WINDOW_SIZE)
        addToDeque(onOffGyroBuffer, averagedGyro, ON_OFF_WINDOW_SIZE)
        addToDeque(onOffStepBuffer, currentSteps, ON_OFF_WINDOW_SIZE)

        Log.d("TremorDebug", "Buffers: tremor=${tremorBuffer.size}/30, freq=${frequencyBuffer.size}/128")

        // 7. Run tremor analysis
        if (tremorBuffer.size >= 30 && frequencyBuffer.size >= 128) {
            Log.d("TremorDebug", "Running analysis...")

            val resultData = tremorAnalyzer.analyze(
                accelBuffer = tremorBuffer.toList(),
                gyroBuffer = gyroBuffer.toList(),
                stepBuffer = stepBuffer.toList(),
                lightBuffer = lightBuffer.toList(),
                deletionBuffer = deletionBuffer.toList(),
                rawAccelList = frequencyBuffer.toList()
            )

            if (resultData != null && !inCooldown) {
                Log.d("TremorDebug", "TREMOR DETECTED! freq=${resultData.avgFrequency}Hz, uploading...")
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Parkinson's Tremor Detected!", Toast.LENGTH_LONG).show()
                }

                // Cache and upload tremor event
                val event = AnomalyEvent(
                    eventType = AnomalyEventType.TREMOR,
                    timestamp = System.currentTimeMillis(),
                    rawAccel = rawAccelRingBuffer.toList(),
                    rawGyro = rawGyroRingBuffer.toList(),
                    aggregatedStats = resultData
                )

                val repo = sensorRepository as? AndroidSensorRepository
                val request = repo?.buildServerRequest(event)
                if (request != null) {
                    val cachedFile = eventCacheManager.cacheEvent(request)
                    val success = repo.uploadRequest(request)
                    if (success && cachedFile != null) {
                        eventCacheManager.removeEvent(cachedFile)
                        Log.d("SensorEvent", "Tremor upload succeeded, cache entry removed")
                    } else {
                        Log.w("SensorEvent", "Tremor upload failed, cached for retry")
                    }
                }

                lastEventTimestamp = System.currentTimeMillis()

                // Clear buffers to reset detection cycle
                tremorBuffer.clear()
                frequencyBuffer.clear()
            } else if (resultData != null) {
                Log.d("TremorDebug", "Tremor detected but in cooldown, skipping upload")
            } else {
                Log.d("TremorDebug", "Analysis returned null (no detection)")
            }
        }

        // 8. Run ON/OFF state evaluation (every 60s, if enough data)
        val onOffNow = System.currentTimeMillis()
        if (onOffAccelBuffer.size >= ON_OFF_MIN_BUFFER &&
            (onOffNow - lastOnOffEvalTimestamp) >= ON_OFF_EVAL_INTERVAL
        ) {
            lastOnOffEvalTimestamp = onOffNow
            evaluateOnOffState()
        }
    }

    private fun loadCachedBaseline() {
        serviceScope.launch {
            try {
                cachedBaseline = onOffStateRepository.getBaseline()
                val count = onOffStateRepository.getSampleCount()
                Log.d("OnOffState", "Loaded baseline: calibrated=${cachedBaseline?.calibrationComplete}, samples=$count")
            } catch (e: Exception) {
                Log.w("OnOffState", "Failed to load baseline: ${e.message}")
            }
        }
    }

    private fun evaluateOnOffState() {
        serviceScope.launch {
            try {
                val metrics = computeActivityMetrics.compute(
                    accelBuffer = onOffAccelBuffer.toList(),
                    gyroBuffer = onOffGyroBuffer.toList(),
                    stepBuffer = onOffStepBuffer.toList()
                ) ?: return@launch

                // Store sample and recompute baseline
                onOffStateRepository.storeSample(metrics)

                // Reload baseline (it may have just become calibrated)
                cachedBaseline = onOffStateRepository.getBaseline()

                val baseline = cachedBaseline
                if (baseline != null && baseline.calibrationComplete) {
                    val result = classifyOnOffState.classify(metrics, baseline)

                    if (result.state != lastOnOffState) {
                        Log.d("OnOffState", "State changed: ${lastOnOffState?.name} → ${result.state.name} (confidence=${result.confidence})")
                        lastOnOffState = result.state

                        // Upload state change
                        uploadOnOffState(result.state, result.confidence, metrics)
                    } else {
                        Log.d("OnOffState", "State: ${result.state.name} (confidence=${result.confidence})")
                    }
                } else {
                    val sampleCount = onOffStateRepository.getSampleCount()
                    Log.d("OnOffState", "Calibrating... samples=$sampleCount, need=${maia.dmt.onoffstate.domain.usecase.ComputeBaselineUseCase.MIN_CALIBRATION_SAMPLES}")
                }
            } catch (e: Exception) {
                Log.w("OnOffState", "Evaluation error: ${e.message}")
            }
        }
    }

    private suspend fun uploadOnOffState(
        state: MedicationState,
        confidence: Float,
        metrics: maia.dmt.onoffstate.domain.model.ActivityMetrics
    ) {
        try {
            val repo = sensorRepository as? AndroidSensorRepository ?: return
            val event = AnomalyEvent(
                eventType = AnomalyEventType.ON_OFF_STATE,
                timestamp = System.currentTimeMillis(),
                rawAccel = emptyList(),
                rawGyro = emptyList(),
                aggregatedStats = null
            )
            val request = repo.buildServerRequest(event)
            if (request != null) {
                val success = repo.uploadRequest(request)
                if (success) {
                    Log.d("OnOffState", "Upload succeeded: state=$state")
                } else {
                    Log.w("OnOffState", "Upload failed for state=$state")
                }
            }
        } catch (e: Exception) {
            Log.w("OnOffState", "Upload error: ${e.message}")
        }
    }

    private fun startPostEventCapture(eventType: AnomalyEventType, aggregatedStats: maia.dmt.core.domain.sensors.model.SensorsData?) {
        pendingEventType = eventType
        pendingAggregatedStats = aggregatedStats
        preEventAccelSnapshot = rawAccelRingBuffer.toList()
        preEventGyroSnapshot = rawGyroRingBuffer.toList()
        postEventAccel.clear()
        postEventGyro.clear()
        postEventCaptureRemaining = POST_EVENT_CYCLES
    }

    private suspend fun uploadPendingEvent() {
        val eventType = pendingEventType ?: return

        val allAccel = preEventAccelSnapshot + postEventAccel
        val allGyro = preEventGyroSnapshot + postEventGyro

        val event = AnomalyEvent(
            eventType = eventType,
            timestamp = System.currentTimeMillis(),
            rawAccel = allAccel,
            rawGyro = allGyro,
            aggregatedStats = pendingAggregatedStats
        )

        Log.d("SensorEvent", "Processing ${event.eventType}: rawAccel=${event.rawAccel.size}, rawGyro=${event.rawGyro.size}")

        val repo = sensorRepository as? AndroidSensorRepository
        val request = repo?.buildServerRequest(event)
        if (request != null) {
            val cachedFile = eventCacheManager.cacheEvent(request)
            val success = repo.uploadRequest(request)
            if (success && cachedFile != null) {
                eventCacheManager.removeEvent(cachedFile)
                Log.d("SensorEvent", "${event.eventType} upload succeeded, cache entry removed")
            } else {
                Log.w("SensorEvent", "${event.eventType} upload failed, cached for retry")
            }
        }

        lastEventTimestamp = System.currentTimeMillis()

        // Clear state
        pendingEventType = null
        pendingAggregatedStats = null
        preEventAccelSnapshot = emptyList()
        preEventGyroSnapshot = emptyList()
        postEventAccel.clear()
        postEventGyro.clear()
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

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        val restartIntent = Intent(this, SensorService::class.java).apply {
            action = "maia.dmt.ACTION_START_SENSOR_SERVICE"
            setPackage(packageName)
        }
        val pendingIntent = PendingIntent.getService(
            this, RESTART_REQUEST_CODE, restartIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 3000,
            pendingIntent
        )
        Log.d("SensorService", "App removed from recents, scheduled restart in 3s")
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}
