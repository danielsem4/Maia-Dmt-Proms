package maia.dmt.home.presentation.module

import androidx.compose.ui.graphics.vector.ImageVector

data class ModuleUiModel(
    val icon: ImageVector,
    val text: String,
    val onClick: () -> Unit
)
