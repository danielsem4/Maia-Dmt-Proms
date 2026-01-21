package maia.dmt.core.domain.sensors.manager

interface SensorServiceManager {
    fun startMonitoring()
    fun stopMonitoring()
    fun isServiceRunning(): Boolean
}