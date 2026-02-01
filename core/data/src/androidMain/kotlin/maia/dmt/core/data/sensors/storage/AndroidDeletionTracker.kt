package maia.dmt.core.data.sensors.storage

import android.content.Context
import maia.dmt.core.domain.sensors.storage.DeletionTracker
import java.io.File
import java.io.IOException

class AndroidDeletionTracker(private val context: Context) : DeletionTracker {

    // We use a physical file to ensure data is shared between the Keyboard Process
    // and the Main App Process without memory caching issues.
    private val deletionFile: File by lazy {
        File(context.filesDir, "keyboard_deletions.dat")
    }

    @Synchronized
    override fun incrementDeleteCount() {
        try {
            val current = readCountFromFile()
            writeCountToFile(current + 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    override fun getDeleteCount(): Int {
        return readCountFromFile()
    }

    @Synchronized
    override fun resetDeleteCount() {
        writeCountToFile(0)
    }

    private fun readCountFromFile(): Int {
        if (!deletionFile.exists()) return 0
        return try {
            // Read text, trim whitespace, convert to Int
            deletionFile.readText().trim().toIntOrNull() ?: 0
        } catch (e: IOException) {
            0
        }
    }

    private fun writeCountToFile(count: Int) {
        try {
            deletionFile.writeText(count.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}