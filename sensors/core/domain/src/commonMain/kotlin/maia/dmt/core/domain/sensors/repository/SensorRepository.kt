package maia.dmt.core.domain.sensors.repository


import kotlinx.coroutines.flow.Flow
import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.LightLevel
import maia.dmt.core.domain.sensors.model.StepCount

interface SensorRepository {
    fun getAcceleration(): Flow<Acceleration>
    fun getGyroscope(): Flow<Gyroscope>
    fun getLightLevel(): Flow<LightLevel>
    fun getStepCount(): Flow<StepCount>

    fun startListening()
    fun stopListening()
}