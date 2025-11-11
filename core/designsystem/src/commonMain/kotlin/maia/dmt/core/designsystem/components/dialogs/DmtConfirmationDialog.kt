package maia.dmt.core.designsystem.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.dismiss_dialog
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtConfirmationDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean = false,
) {
    val deviceConfig = currentDeviceConfiguration()

    val dialogSizing = when(deviceConfig) {
        DeviceConfiguration.MOBILE_PORTRAIT -> DialogSizing(
            maxWidth = 340.dp,
            horizontalPadding = 16.dp,
            verticalPadding = 12.dp,
            contentPadding = 20.dp,
            cornerRadius = 12.dp,
            spaceBetweenElements = 12.dp,
            buttonSpacing = 8.dp,
            topButtonPadding = 12.dp,
            titleStyle = MaterialTheme.typography.titleSmall,
            descriptionStyle = MaterialTheme.typography.bodySmall
        )
        DeviceConfiguration.MOBILE_LANDSCAPE -> DialogSizing(
            maxWidth = 400.dp,
            horizontalPadding = 20.dp,
            verticalPadding = 14.dp,
            contentPadding = 22.dp,
            cornerRadius = 14.dp,
            spaceBetweenElements = 14.dp,
            buttonSpacing = 10.dp,
            topButtonPadding = 14.dp,
            titleStyle = MaterialTheme.typography.titleMedium,
            descriptionStyle = MaterialTheme.typography.bodyMedium
        )
        DeviceConfiguration.TABLET_PORTRAIT -> DialogSizing(
            maxWidth = 480.dp,
            horizontalPadding = 24.dp,
            verticalPadding = 16.dp,
            contentPadding = 24.dp,
            cornerRadius = 16.dp,
            spaceBetweenElements = 16.dp,
            buttonSpacing = 12.dp,
            topButtonPadding = 16.dp,
            titleStyle = MaterialTheme.typography.titleLarge,
            descriptionStyle = MaterialTheme.typography.bodyMedium
        )
        DeviceConfiguration.TABLET_LANDSCAPE -> DialogSizing(
            maxWidth = 560.dp,
            horizontalPadding = 28.dp,
            verticalPadding = 18.dp,
            contentPadding = 28.dp,
            cornerRadius = 18.dp,
            spaceBetweenElements = 18.dp,
            buttonSpacing = 14.dp,
            topButtonPadding = 18.dp,
            titleStyle = MaterialTheme.typography.headlineLarge,
            descriptionStyle = MaterialTheme.typography.bodyLarge
        )
        DeviceConfiguration.DESKTOP -> DialogSizing(
            maxWidth = 640.dp,
            horizontalPadding = 32.dp,
            verticalPadding = 20.dp,
            contentPadding = 32.dp,
            cornerRadius = 20.dp,
            spaceBetweenElements = 20.dp,
            buttonSpacing = 16.dp,
            topButtonPadding = 20.dp,
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
                    .padding(dialogSizing.contentPadding),
                verticalArrangement = Arrangement.spacedBy(dialogSizing.spaceBetweenElements),
            ) {
                Text(
                    text = title,
                    style = dialogSizing.titleStyle,
                    color = MaterialTheme.colorScheme.extended.textPrimary
                )
                Text(
                    text = description,
                    style = dialogSizing.descriptionStyle,
                    color = MaterialTheme.colorScheme.extended.textSecondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dialogSizing.topButtonPadding),
                    horizontalArrangement = Arrangement.spacedBy(
                        dialogSizing.buttonSpacing,
                        Alignment.End
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DmtButton(
                        text = cancelButtonText,
                        onClick = onCancelClick,
                        style = DmtButtonStyle.SECONDARY
                    )
                    DmtButton(
                        text = confirmButtonText,
                        isLoading = isLoading,
                        onClick = onConfirmClick,
                        style = DmtButtonStyle.DESTRUCTIVE_PRIMARY
                    )
                }
            }
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

private data class DialogSizing(
    val maxWidth: androidx.compose.ui.unit.Dp,
    val horizontalPadding: androidx.compose.ui.unit.Dp,
    val verticalPadding: androidx.compose.ui.unit.Dp,
    val contentPadding: androidx.compose.ui.unit.Dp,
    val cornerRadius: androidx.compose.ui.unit.Dp,
    val spaceBetweenElements: androidx.compose.ui.unit.Dp,
    val buttonSpacing: androidx.compose.ui.unit.Dp,
    val topButtonPadding: androidx.compose.ui.unit.Dp,
    val titleStyle: TextStyle,
    val descriptionStyle: TextStyle
)

@Composable
@Preview
fun DmtConfirmationDialogPreview() {
    DmtTheme {
        DmtConfirmationDialog(
            title = "Delete profile picture?",
            description = "This will permanently delete your profile picture. This cannot be undone.",
            confirmButtonText = "Delete",
            cancelButtonText = "Cancel",
            onConfirmClick = {},
            onCancelClick = {},
            onDismiss = {}
        )
    }
}