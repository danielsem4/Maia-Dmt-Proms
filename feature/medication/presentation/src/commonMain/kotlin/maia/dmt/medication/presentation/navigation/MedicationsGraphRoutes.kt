package maia.dmt.medication.presentation.navigation

import kotlinx.serialization.Serializable

interface MedicationsGraphRoutes {

    @Serializable
    data object Graph: MedicationsGraphRoutes

    @Serializable
    data object Medications: MedicationsGraphRoutes

    @Serializable
    data object AllMedications: MedicationsGraphRoutes

    @Serializable
    data object MedicationReminder: MedicationsGraphRoutes




}