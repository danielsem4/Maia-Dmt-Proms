package maia.dmt.core.designsystem.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.dismiss_dialog
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtContentDialog(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    showCloseButton: Boolean = true,
    content: @Composable () -> Unit
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
                modifier = modifier
                    .padding(24.dp)
                    .padding(top = if (showCloseButton) 8.dp else 0.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.extended.textPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                content()
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
fun DmtContentDialogPreview() {
    DmtTheme {
        DmtContentDialog(
            title = "Select Pain Areas",
            onDismiss = {},
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Please select all that apply:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.extended.textSecondary
                    )

                    repeat(3) { index ->
                        Text(
                            text = "Option ${index + 1}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        )
    }
}