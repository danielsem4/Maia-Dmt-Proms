package maia.dmt.core.domain.sensors.manager

import android.content.Context
import android.content.Intent

class AndroidSensorServiceManager(
    private val context: Context
) : SensorServiceManager {

    companion object {
        private const val SERVICE_ACTION = "maia.dmt.ACTION_START_SENSOR_SERVICE"
    }

    override fun startMonitoring() {
        val intent = Intent(SERVICE_ACTION).apply {
            setPackage(context.packageName)
        }

        try {
            context.startForegroundService(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun stopMonitoring() {
        val intent = Intent(SERVICE_ACTION).apply {
            setPackage(context.packageName)
        }
        context.stopService(intent)
    }

    override fun isServiceRunning(): Boolean {
        return false
    }
}