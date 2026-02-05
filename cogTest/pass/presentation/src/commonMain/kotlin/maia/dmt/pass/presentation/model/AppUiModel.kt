package maia.dmt.pass.presentation.model

import androidx.compose.ui.graphics.Color
import maia.dmt.pass.presentation.passApps.AppType
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class AppUiModel(
    val type: AppType,
    val nameRes: StringResource,
    val iconRes: DrawableResource,
    val color: Color = Color.Blue
)
