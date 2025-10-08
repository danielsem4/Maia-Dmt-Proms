package maia.dmt.medication.presentation.navigation

import kotlinx.serialization.Serializable

interface MedicationsGraphRoutes {

    @Serializable
    data object Graph: MedicationsGraphRoutes

    @Serializable
    data object Medications: MedicationsGraphRoutes

    @Serializable
    data class AllMedications(val isReport: Boolean) : MedicationsGraphRoutes

    @Serializable
    data object MedicationReminder: MedicationsGraphRoutes


}