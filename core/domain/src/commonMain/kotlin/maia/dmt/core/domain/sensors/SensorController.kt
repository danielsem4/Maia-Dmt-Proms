package maia.dmt.core.domain.sensors

interface SensorController {
    fun hasSensorPermission(): Boolean
    fun startSensorService()
}