package maia.dmt.core.designsystem.preview

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.arrow_left_icon
import maia.dmt.core.designsystem.components.layouts.DmtAdaptiveFormLayout
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.vectorResource

@Composable
@PreviewLightDark
@PreviewScreenSizes
fun DmtAdaptiveFormLayoutLightPreview() {
    DmtTheme {
        DmtAdaptiveFormLayout(
            headerText = "Welcome to Dmt!",
            errorText = "Login failed!",
            logo = {
                Icon(
                    imageVector = vectorResource(Res.drawable.arrow_left_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            formContent = {
                Text(
                    text = "Sample form title",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Sample form title 2",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}