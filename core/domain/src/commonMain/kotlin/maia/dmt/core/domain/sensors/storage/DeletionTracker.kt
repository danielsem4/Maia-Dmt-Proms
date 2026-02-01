package maia.dmt.core.domain.sensors.storage

interface DeletionTracker {
    fun incrementDeleteCount()
    fun getDeleteCount(): Int
    fun resetDeleteCount()
}