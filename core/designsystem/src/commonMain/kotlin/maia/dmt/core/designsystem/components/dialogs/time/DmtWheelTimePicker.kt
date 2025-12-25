package maia.dmt.core.designsystem.components.dialogs.time

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.generic_cancel
import dmtproms.core.designsystem.generated.resources.generic_ok
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.ui.timepicker.WheelTimePickerComponent.WheelTimePicker
import network.chaintech.kmp_date_time_picker.utils.TimeFormat
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DmtWheelTimePicker(
    title: String,
    initialHour: Int? = null,
    initialMinute: Int? = null,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
    is24Hour: Boolean = true
) {
    val currentMoment = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    val startHour = initialHour ?: currentMoment.hour
    val startMinute = initialMinute ?: currentMoment.minute

    var selectedTime by remember(startHour, startMinute) {
        mutableStateOf(LocalTime(startHour, startMinute))
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.9f)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    WheelTimePicker(
                        modifier = Modifier.fillMaxWidth(),
                        startTime = selectedTime,
                        timeFormat = if (is24Hour) TimeFormat.HOUR_24 else TimeFormat.AM_PM,
                        rowCount = 5,
                        height = 180.dp,
                        selectorProperties = WheelPickerDefaults.selectorProperties(
                            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            enabled = true
                        ),
                        hideHeader = true,
                        title = "",
                        doneLabel = "",
                        titleStyle = TextStyle.Default,
                        doneLabelStyle = TextStyle.Default,
                        onDoneClick = { },
                        onTimeChangeListener = { newTime ->
                            selectedTime = newTime
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(Res.string.generic_cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirm(selectedTime.hour, selectedTime.minute)
                        }
                    ) {
                        Text(stringResource(Res.string.generic_ok))
                    }
                }
            }
        }
    }
}