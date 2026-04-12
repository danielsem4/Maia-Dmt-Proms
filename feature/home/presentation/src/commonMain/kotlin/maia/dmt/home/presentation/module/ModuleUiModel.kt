package maia.dmt.home.presentation.module

import org.jetbrains.compose.resources.DrawableResource
import maia.dmt.core.presentation.util.UiText

data class ModuleUiModel(
    val icon: DrawableResource,
    val text: UiText,
    val onClick: () -> Unit
)
