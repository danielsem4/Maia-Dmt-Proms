package maia.dmt.fileshare.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.dialogs.DmtContentDialog
import maia.dmt.core.designsystem.components.textFields.DmtTextField
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.core.presentation.util.UiText
import maia.dmt.fileshare.presentation.fileList.FileListAction

@Composable
fun FileUploadDialog(
    pickedFileName: String,
    customFileName: String,
    isUploading: Boolean,
    uploadError: UiText?,
    hasFilePicked: Boolean,
    onPickFileClick: () -> Unit,
    onAction: (FileListAction) -> Unit,
    onDismiss: () -> Unit
) {
    val nameTextState = rememberTextFieldState(initialText = customFileName)

    LaunchedEffect(customFileName) {
        if (customFileName != nameTextState.text.toString()) {
            nameTextState.edit {
                replace(0, length, customFileName)
            }
        }
    }

    LaunchedEffect(nameTextState.text) {
        val newText = nameTextState.text.toString()
        if (newText != customFileName) {
            onAction(FileListAction.OnCustomFileNameChange(newText))
        }
    }

    DmtContentDialog(
        title = "Upload File",
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!hasFilePicked) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Select a file to upload",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.extended.textSecondary
                    )

                    DmtButton(
                        text = "Choose File",
                        onClick = onPickFileClick,
                        style = DmtButtonStyle.SECONDARY,
                        enabled = !isUploading
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = pickedFileName,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                DmtButton(
                    text = "Change File",
                    onClick = onPickFileClick,
                    style = DmtButtonStyle.TEXT,
                    enabled = !isUploading
                )
            }

            if (hasFilePicked) {
                DmtTextField(
                    state = nameTextState,
                    title = "File Name",
                    placeholder = "Enter file name",
                    singleLine = true,
                    enabled = !isUploading,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (uploadError != null) {
                Text(
                    text = uploadError.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            DmtButton(
                text = "Upload",
                onClick = { onAction(FileListAction.OnUploadClick) },
                style = DmtButtonStyle.PRIMARY,
                enabled = hasFilePicked && !isUploading,
                isLoading = isUploading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
