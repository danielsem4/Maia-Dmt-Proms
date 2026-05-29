package maia.dmt.proms.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Boot completed, restarting SensorService")
            val serviceIntent = Intent("maia.dmt.ACTION_START_SENSOR_SERVICE").apply {
                setPackage(context.packageName)
            }
            try {
                context.startForegroundService(serviceIntent)
            } catch (e: Exception) {
                Log.e("BootReceiver", "Failed to restart SensorService: ${e.message}", e)
            }
        }
    }
}
