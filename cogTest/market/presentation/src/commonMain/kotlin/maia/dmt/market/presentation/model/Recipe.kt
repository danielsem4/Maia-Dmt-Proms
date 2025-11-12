package maia.dmt.market.presentation.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class Recipe(
    val id: String,
    val titleRes: StringResource,
    val imageRes: DrawableResource
)