package maia.dmt.core.designsystem.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.dismiss_dialog
import dmtproms.core.designsystem.generated.resources.eye_icon
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtCustomDialog(
    icon: DrawableResource,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    title: String,
    description: String,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    onDismiss: () -> Unit,
    primaryButtonStyle: DmtButtonStyle = DmtButtonStyle.PRIMARY,
    secondaryButtonStyle: DmtButtonStyle = DmtButtonStyle.SECONDARY,
    showCloseButton: Boolean = true
) {
    val deviceConfig = currentDeviceConfiguration()

    val dialogSizing = when(deviceConfig) {
        DeviceConfiguration.MOBILE_PORTRAIT -> CustomDialogSizing(
            maxWidth = 340.dp,
            horizontalPadding = 16.dp,
            verticalPadding = 12.dp,
            contentPadding = 12.dp,
            topPadding = 8.dp,
            cornerRadius = 12.dp,
            spaceBetweenElements = 12.dp,
            buttonSpacing = 8.dp,
            topButtonPadding = 8.dp,
            iconSize = 40.dp,
            titleStyle = MaterialTheme.typography.titleSmall,
            descriptionStyle = MaterialTheme.typography.bodySmall
        )
        DeviceConfiguration.MOBILE_LANDSCAPE -> CustomDialogSizing(
            maxWidth = 400.dp,
            horizontalPadding = 20.dp,
            verticalPadding = 14.dp,
            contentPadding = 14.dp,
            topPadding = 8.dp,
            cornerRadius = 14.dp,
            spaceBetweenElements = 14.dp,
            buttonSpacing = 10.dp,
            topButtonPadding = 10.dp,
            iconSize = 44.dp,
            titleStyle = MaterialTheme.typography.titleMedium,
            descriptionStyle = MaterialTheme.typography.bodyMedium
        )
        DeviceConfiguration.TABLET_PORTRAIT -> CustomDialogSizing(
            maxWidth = 480.dp,
            horizontalPadding = 24.dp,
            verticalPadding = 16.dp,
            contentPadding = 16.dp,
            topPadding = 8.dp,
            cornerRadius = 16.dp,
            spaceBetweenElements = 16.dp,
            buttonSpacing = 12.dp,
            topButtonPadding = 12.dp,
            iconSize = 48.dp,
            titleStyle = MaterialTheme.typography.headlineLarge,
            descriptionStyle = MaterialTheme.typography.bodyMedium
        )
        DeviceConfiguration.TABLET_LANDSCAPE -> CustomDialogSizing(
            maxWidth = 560.dp,
            horizontalPadding = 28.dp,
            verticalPadding = 18.dp,
            contentPadding = 20.dp,
            topPadding = 10.dp,
            cornerRadius = 18.dp,
            spaceBetweenElements = 18.dp,
            buttonSpacing = 14.dp,
            topButtonPadding = 14.dp,
            iconSize = 56.dp,
            titleStyle = MaterialTheme.typography.headlineLarge,
            descriptionStyle = MaterialTheme.typography.bodyLarge
        )
        DeviceConfiguration.DESKTOP -> CustomDialogSizing(
            maxWidth = 640.dp,
            horizontalPadding = 32.dp,
            verticalPadding = 20.dp,
            contentPadding = 24.dp,
            topPadding = 12.dp,
            cornerRadius = 20.dp,
            spaceBetweenElements = 20.dp,
            buttonSpacing = 16.dp,
            topButtonPadding = 16.dp,
            iconSize = 64.dp,
            titleStyle = MaterialTheme.typography.headlineLarge,
            descriptionStyle = MaterialTheme.typography.bodyLarge
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .padding(
                    horizontal = dialogSizing.horizontalPadding,
                    vertical = dialogSizing.verticalPadding
                )
                .widthIn(max = dialogSizing.maxWidth)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(dialogSizing.cornerRadius)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dialogSizing.contentPadding)
                    .padding(top = if (showCloseButton) dialogSizing.topPadding else 0.dp),
                verticalArrangement = Arrangement.spacedBy(dialogSizing.spaceBetweenElements),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = vectorResource(icon),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(dialogSizing.iconSize)
                )

                Text(
                    text = title,
                    style = dialogSizing.titleStyle,
                    color = MaterialTheme.colorScheme.extended.textPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = description,
                    style = dialogSizing.descriptionStyle,
                    color = MaterialTheme.colorScheme.extended.textSecondary,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dialogSizing.topButtonPadding),
                    horizontalArrangement = Arrangement.spacedBy(dialogSizing.buttonSpacing),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DmtButton(
                        text = secondaryButtonText,
                        onClick = onSecondaryClick,
                        style = secondaryButtonStyle,
                        modifier = Modifier.weight(1f)
                    )
                    DmtButton(
                        text = primaryButtonText,
                        onClick = onPrimaryClick,
                        style = primaryButtonStyle,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (showCloseButton) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.dismiss_dialog),
                        tint = MaterialTheme.colorScheme.extended.textSecondary
                    )
                }
            }
        }
    }
}

private data class CustomDialogSizing(
    val maxWidth: androidx.compose.ui.unit.Dp,
    val horizontalPadding: androidx.compose.ui.unit.Dp,
    val verticalPadding: androidx.compose.ui.unit.Dp,
    val contentPadding: androidx.compose.ui.unit.Dp,
    val topPadding: androidx.compose.ui.unit.Dp,
    val cornerRadius: androidx.compose.ui.unit.Dp,
    val spaceBetweenElements: androidx.compose.ui.unit.Dp,
    val buttonSpacing: androidx.compose.ui.unit.Dp,
    val topButtonPadding: androidx.compose.ui.unit.Dp,
    val iconSize: androidx.compose.ui.unit.Dp,
    val titleStyle: TextStyle,
    val descriptionStyle: TextStyle
)

@Composable
@Preview
fun DmtCustomDialogPreview() {
    DmtTheme {
        DmtCustomDialog(
            icon = Res.drawable.eye_icon,
            iconTint = MaterialTheme.colorScheme.primary,
            title = "Confirm Action",
            description = "Are you sure you want to proceed with this action? This will make changes to your account.",
            primaryButtonText = "Confirm",
            secondaryButtonText = "Cancel",
            onPrimaryClick = {},
            onSecondaryClick = {},
            onDismiss = {},
            primaryButtonStyle = DmtButtonStyle.PRIMARY,
            secondaryButtonStyle = DmtButtonStyle.SECONDARY
        )
    }
}

@Composable
@Preview
fun DmtCustomDialogLongTextPreview() {
    DmtTheme(darkTheme = true) {
        DmtCustomDialog(
            icon = Res.drawable.eye_icon,
            iconTint = MaterialTheme.colorScheme.primary,
            title = "Rising",
            description = "I made the activity\nAt:21/10/2025 at 15:42",
            primaryButtonText = "Report",
            secondaryButtonText = "Date & Time",
            onPrimaryClick = {},
            onSecondaryClick = {},
            onDismiss = {},
            primaryButtonStyle = DmtButtonStyle.PRIMARY,
            secondaryButtonStyle = DmtButtonStyle.SECONDARY
        )
    }
}