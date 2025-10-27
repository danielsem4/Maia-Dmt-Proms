package maia.dmt.medication.presentation.di

import maia.dmt.medication.presentation.allMedications.AllMedicationViewModel
import maia.dmt.medication.presentation.medicationStatistics.MedicationStatisticsViewModel
import maia.dmt.medication.presentation.medications.MedicationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val medicationPresentationModule = module {
    viewModelOf(::AllMedicationViewModel)
    viewModelOf(::MedicationViewModel)
    viewModelOf(::MedicationStatisticsViewModel)
}