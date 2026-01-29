package maia.dmt.core.data.sensors.di

import maia.dmt.core.data.sensors.IosSensorController
import maia.dmt.core.domain.sensors.SensorController
import maia.dmt.core.domain.sensors.manager.IosSensorServiceManager
import maia.dmt.core.domain.sensors.manager.SensorServiceManager
import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.LightLevel
import maia.dmt.core.domain.sensors.model.StepCount
import maia.dmt.core.domain.sensors.repository.SensorRepository
import org.koin.dsl.module

actual val platformsSensorsCoreDataModule = module {
    single<SensorRepository> {
        object : SensorRepository {
            override fun getAcceleration() = kotlinx.coroutines.flow.emptyFlow<Acceleration>()
            override fun getGyroscope() = kotlinx.coroutines.flow.emptyFlow<Gyroscope>()
            override fun getLightLevel() = kotlinx.coroutines.flow.emptyFlow<LightLevel>()
            override fun getStepCount() = kotlinx.coroutines.flow.emptyFlow<StepCount>()
            override fun startListening() {}
            override fun stopListening() {}
        }
    }

    single<SensorController> { IosSensorController() }
    single<SensorServiceManager> { IosSensorServiceManager() }
}