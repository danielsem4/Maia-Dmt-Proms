package maia.dmt.core.data.sensors

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import maia.dmt.core.domain.sensors.SensorController

class AndroidSensorController(
    private val context: Context
) : SensorController {

    companion object {
        private const val SERVICE_ACTION = "maia.dmt.ACTION_START_SENSOR_SERVICE"
    }

    override fun hasSensorPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= 29) {
            val status = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            )
            status == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    override fun startSensorService() {
        val intent = Intent(SERVICE_ACTION).apply {
            setPackage(context.packageName)
        }

        try {
            context.startForegroundService(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}