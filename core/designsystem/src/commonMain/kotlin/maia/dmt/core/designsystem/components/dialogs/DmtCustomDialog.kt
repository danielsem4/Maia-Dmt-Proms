package maia.dmt.core.designsystem.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtCustomDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    showCloseButton: Boolean = true,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    usePlatformDefaultWidth: Boolean = false,
    maxWidth: Dp = 480.dp,
    horizontalPadding: Dp = 24.dp,
    verticalPadding: Dp = 16.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = usePlatformDefaultWidth
        )
    ) {
        Box(
            modifier = modifier
                .padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                )
                .widthIn(max = maxWidth)
                .background(
                    color = backgroundColor,
                    shape = shape
                )
        ) {
            content()

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
            onDismiss = { /* dismiss */ }
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Delete profile picture?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.extended.textPrimary
                )
                Text(
                    text = "This will permanently delete your profile picture.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textSecondary
                )
                DmtButton(
                    text = "Ok",
                    onClick = { /* confirm */ },
                    style = DmtButtonStyle.PRIMARY
                )
            }
        }
     }
}