package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.components.dialogs.DmtTimePickerDialog
import maia.dmt.core.domain.evaluation.ElementConfig
import maia.dmt.core.domain.evaluation.ElementType
import maia.dmt.core.domain.evaluation.EvaluationElement

class InputTimeElementRenderer : ElementRenderer {
    override fun canRender(elementType: ElementType): Boolean {
        return elementType == ElementType.INPUT_TIME
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Render(
        element: EvaluationElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val config = element.config as? ElementConfig.InputTimeConfig
        val placeholder = config?.placeholder ?: "Select time..."

        val initialHour = currentAnswer.substringBefore(":", "").toIntOrNull() ?: 0
        val initialMinute = currentAnswer.substringAfter(":", "").toIntOrNull() ?: 0

        var showDialog by remember { mutableStateOf(false) }
        val timeState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = true
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true },
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = currentAnswer.ifEmpty { placeholder },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (currentAnswer.isEmpty()) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }

        if (showDialog) {
            DmtTimePickerDialog(
                state = timeState,
                title = element.label.ifEmpty { "Select Time" },
                onDismiss = { showDialog = false },
                onConfirm = {
                    val hour = timeState.hour.toString().padStart(2, '0')
                    val minute = timeState.minute.toString().padStart(2, '0')
                    onAnswerChange("$hour:$minute")
                    showDialog = false
                }
            )
        }
    }
}
