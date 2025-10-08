package maia.dmt.core.designsystem.components.dialogs

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DmtDatePickerDialog(
    initialDateMillis: Long? = null,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )

    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DmtButton(
                text = "OK",
                onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                },
                style = DmtButtonStyle.PRIMARY
            )
        },
        dismissButton = {
            DmtButton(
                text = "Cancel",
                onClick = onDismiss,
                style = DmtButtonStyle.SECONDARY
            )
        }
    ) {
        androidx.compose.material3.DatePicker(state = datePickerState)
    }
}

@Composable
@Preview
fun DmtDatePickerDialogPreview() {
    DmtDatePickerDialog(
        initialDateMillis = Clock.System.now().toEpochMilliseconds(),
        onDateSelected = {},
        onDismiss = {}
    )

}