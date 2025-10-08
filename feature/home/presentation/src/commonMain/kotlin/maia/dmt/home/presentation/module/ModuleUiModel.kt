package maia.dmt.home.presentation.module

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

data class ModuleUiModel(
    val icon: DrawableResource,
    val text: String,
    val onClick: () -> Unit
)
