package maia.dmt.core.domain.repository


import kotlinx.coroutines.flow.Flow
import maia.dmt.core.domain.model.Acceleration
import maia.dmt.core.domain.model.Gyroscope
import maia.dmt.core.domain.model.LightLevel
import maia.dmt.core.domain.model.StepCount

interface SensorRepository {
    fun getAcceleration(): Flow<Acceleration>
    fun getGyroscope(): Flow<Gyroscope>
    fun getLightLevel(): Flow<LightLevel>
    fun getStepCount(): Flow<StepCount>

    fun startListening()
    fun stopListening()
}