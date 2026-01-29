package maia.dmt.core.data.sensors.di

import org.koin.core.module.Module
import org.koin.dsl.module


expect val platformsSensorsCoreDataModule: Module

val sensorsCoreDataModule = module {
    includes(platformsSensorsCoreDataModule)
}