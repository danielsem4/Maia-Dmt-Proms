package maia.dmt.proms.services

import android.content.Context
import android.util.Log
import maia.dmt.core.data.dto.sensors.SensorsDataServerRequest
import maia.dmt.core.data.util.defaultJson
import java.io.File
import java.util.UUID

class EventCacheManager(context: Context) {

    private val cacheDir: File = File(context.filesDir, "pending_events").also {
        it.mkdirs()
    }

    companion object {
        private const val TAG = "EventCacheManager"
        private const val MAX_CACHED_EVENTS = 500
    }

    fun cacheEvent(request: SensorsDataServerRequest): File? {
        return try {
            pruneIfNeeded()
            val filename = "event_${System.currentTimeMillis()}_${UUID.randomUUID()}.json"
            val tmpFile = File(cacheDir, "$filename.tmp")
            val targetFile = File(cacheDir, filename)
            val jsonString = defaultJson.encodeToString(SensorsDataServerRequest.serializer(), request)
            tmpFile.writeText(jsonString)
            tmpFile.renameTo(targetFile)
            Log.d(TAG, "Cached event: $filename")
            targetFile
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cache event: ${e.message}", e)
            null
        }
    }

    fun getPendingEvents(): List<Pair<File, SensorsDataServerRequest>> {
        val files = cacheDir.listFiles { f -> f.extension == "json" }
            ?.sortedBy { it.name }
            ?: return emptyList()
        return files.mapNotNull { file ->
            try {
                val request = defaultJson.decodeFromString(
                    SensorsDataServerRequest.serializer(), file.readText()
                )
                file to request
            } catch (e: Exception) {
                Log.e(TAG, "Corrupt cache file ${file.name}, deleting", e)
                file.delete()
                null
            }
        }
    }

    fun removeEvent(file: File) {
        file.delete()
    }

    private fun pruneIfNeeded() {
        val files = cacheDir.listFiles { f -> f.extension == "json" }
            ?.sortedBy { it.name } ?: return
        if (files.size >= MAX_CACHED_EVENTS) {
            val toRemove = files.size - MAX_CACHED_EVENTS + 1
            files.take(toRemove).forEach { it.delete() }
            Log.w(TAG, "Pruned $toRemove oldest cached events")
        }
    }
}
