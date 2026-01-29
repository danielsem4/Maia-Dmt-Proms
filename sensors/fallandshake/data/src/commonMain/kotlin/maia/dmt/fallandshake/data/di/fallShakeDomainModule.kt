package maia.dmt.fallandshake.data.di

import maia.dmt.fallandshake.domain.usecase.DetectFallUseCase
import maia.dmt.fallandshake.domain.usecase.DetectTremorUseCase
import maia.dmt.fallandshake.domain.usecase.ProcessAccelerometerDataUseCase
import org.koin.dsl.module

val fallShakeDataModule = module {
    factory { DetectFallUseCase() }
    factory { ProcessAccelerometerDataUseCase() }
    factory { DetectTremorUseCase() }
}