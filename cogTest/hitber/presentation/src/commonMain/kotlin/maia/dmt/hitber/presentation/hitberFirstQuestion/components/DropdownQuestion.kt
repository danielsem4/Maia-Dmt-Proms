package maia.dmt.hitber.presentation.hitberFirstQuestion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.components.select.DmtDropDown
import maia.dmt.core.domain.dto.evaluation.EvaluationObject

@Composable
fun DropdownQuestion(
    question: EvaluationObject,
    currentAnswer: String,
    onAnswerSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedValue = question.available_values.find { it.available_value == currentAnswer }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = question.object_label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        DmtDropDown(
            items = question.available_values,
            selectedItem = selectedValue,
            onItemSelected = { value -> onAnswerSelected(value.available_value) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = question.object_label,
            itemContent = { value ->
                Text(
                    text = value.available_value,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            selectedItemContent = { value ->
                Text(
                    text = value.available_value,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            borderColor = MaterialTheme.colorScheme.primary,
            contentPadding = PaddingValues(20.dp),
        )
    }
}
