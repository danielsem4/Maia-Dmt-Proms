package maia.dmt.core.designsystem.components.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DmtDatePickerDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
    initialDateMillis: Long? = null
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis ?: Clock.System.now().toEpochMilliseconds()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { onConfirm(it) }
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(16.dp)
                )
            }
        )
    }
}

private fun validateDate(date: LocalDate, restrictionType: RestrictionType): Boolean {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    return when (restrictionType) {
        RestrictionType.NoPastDates -> date >= today
        else -> true
    }
}

enum class RestrictionType {
    None, NoPastDates
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DmtDatePickerDialogPreview() {
    DmtDatePickerDialog(
        title = "Select Date",
        onDismiss = {},
        onConfirm = {}
    )
}