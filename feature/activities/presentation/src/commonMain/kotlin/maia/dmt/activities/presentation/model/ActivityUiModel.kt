package maia.dmt.activities.presentation.model

import org.jetbrains.compose.resources.DrawableResource

data class ActivityUiModel(
    val id: String,
    val text: String,
    val icon: DrawableResource,
    val onClick: () -> Unit
)
