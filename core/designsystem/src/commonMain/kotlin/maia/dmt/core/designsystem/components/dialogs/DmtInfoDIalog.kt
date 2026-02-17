package maia.dmt.core.designsystem.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
fun DmtInfoDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val deviceConfig = currentDeviceConfiguration()

    val dialogSizing = when(deviceConfig) {
        DeviceConfiguration.MOBILE_PORTRAIT -> InfoDialogSizing(
            maxWidth = 340.dp,
            contentPadding = 24.dp,
            cornerRadius = 16.dp,
            verticalSpacing = 16.dp,
            titleStyle = MaterialTheme.typography.titleMedium,
            descriptionStyle = MaterialTheme.typography.bodySmall
        )
        DeviceConfiguration.MOBILE_LANDSCAPE -> InfoDialogSizing(
            maxWidth = 400.dp,
            contentPadding = 20.dp,
            cornerRadius = 16.dp,
            verticalSpacing = 14.dp,
            titleStyle = MaterialTheme.typography.titleMedium,
            descriptionStyle = MaterialTheme.typography.bodyMedium
        )
        DeviceConfiguration.TABLET_PORTRAIT -> InfoDialogSizing(
            maxWidth = 480.dp,
            contentPadding = 32.dp,
            cornerRadius = 20.dp,
            verticalSpacing = 24.dp,
            titleStyle = MaterialTheme.typography.titleLarge,
            descriptionStyle = MaterialTheme.typography.titleMedium
        )
        DeviceConfiguration.TABLET_LANDSCAPE, DeviceConfiguration.DESKTOP -> InfoDialogSizing(
            maxWidth = 560.dp,
            contentPadding = 40.dp,
            cornerRadius = 24.dp,
            verticalSpacing = 32.dp,
            titleStyle = MaterialTheme.typography.titleLarge,
            descriptionStyle = MaterialTheme.typography.titleMedium
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
                .padding(16.dp)
                .widthIn(max = dialogSizing.maxWidth)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(dialogSizing.cornerRadius)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(dialogSizing.contentPadding),
                verticalArrangement = Arrangement.spacedBy(dialogSizing.verticalSpacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = dialogSizing.titleStyle,
                    color = MaterialTheme.colorScheme.extended.textPrimary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = dialogSizing.descriptionStyle,
                    color = MaterialTheme.colorScheme.extended.textSecondary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )

                DmtButton(
                    text = confirmButtonText,
                    onClick = onConfirmClick,
                    style = DmtButtonStyle.PRIMARY,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Close Icon
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
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

private data class InfoDialogSizing(
    val maxWidth: Dp,
    val contentPadding: Dp,
    val cornerRadius: Dp,
    val verticalSpacing: Dp,
    val titleStyle: TextStyle,
    val descriptionStyle: TextStyle
)

@Composable
@Preview
fun DmtInfoDialogPreview() {
    DmtTheme {
        DmtInfoDialog(
            title = "Look at the images",
            description = "You have 30 seconds to memorize the shape pairs shown on screen.",
            confirmButtonText = "Start",
            onConfirmClick = {},
            onDismiss = {}
        )
    }
}