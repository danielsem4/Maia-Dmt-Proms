package maia.dmt.medication.data.di

import maia.dmt.medication.data.medications.KtorMedicationService
import maia.dmt.medication.domain.medications.MedicationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val medicationDataModule = module {
    singleOf(::KtorMedicationService) bind MedicationService::class
}