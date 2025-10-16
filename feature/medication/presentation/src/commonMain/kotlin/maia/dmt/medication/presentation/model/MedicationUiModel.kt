package maia.dmt.medication.presentation.model

import org.jetbrains.compose.resources.DrawableResource

data class MedicationUiModel(
    val text: String,
    val id: String,
    val onClick: () -> Unit,
    val icon: DrawableResource? = null
)
