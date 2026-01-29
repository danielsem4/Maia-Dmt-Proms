package maia.dmt.core.data.sensors.di

import maia.dmt.core.data.sensors.AndroidSensorController
import maia.dmt.core.data.sensors.AndroidSensorRepository
import maia.dmt.core.domain.sensors.SensorController
import maia.dmt.core.domain.sensors.manager.AndroidSensorServiceManager
import maia.dmt.core.domain.sensors.manager.SensorServiceManager
import maia.dmt.core.domain.sensors.repository.SensorRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformsSensorsCoreDataModule = module {
    single<SensorRepository> {
        AndroidSensorRepository(
            androidContext(),
            sensorsService = get(),
            sessionStorage = get()
        )
    }
    single<SensorController> { AndroidSensorController(androidContext()) }
    single<SensorServiceManager> { AndroidSensorServiceManager(androidContext()) }
}