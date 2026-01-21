package maia.dmt.core.data.sensors

import maia.dmt.core.domain.sensors.SensorController

class IosSensorController : SensorController {

    override fun hasSensorPermission(): Boolean = true

    // Do nothing on iOS
    override fun startSensorService() {
        // No-op 
    }
}