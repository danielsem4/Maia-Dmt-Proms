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
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
                .widthIn(max = 480.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .padding(top = if (showCloseButton) 8.dp else 0.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = vectorResource(icon),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.extended.textPrimary
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textSecondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
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