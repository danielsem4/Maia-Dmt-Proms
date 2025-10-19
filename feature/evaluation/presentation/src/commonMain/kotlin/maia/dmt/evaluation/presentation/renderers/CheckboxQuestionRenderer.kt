package maia.dmt.evaluation.presentation.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.select.CheckboxOption
import maia.dmt.core.designsystem.components.select.DmtCheckBoxGroup
import maia.dmt.core.designsystem.components.select.DmtCheckboxCardGroup
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.evaluation.presentation.evaluation.EvaluationObjectType

class CheckboxQuestionRenderer : QuestionRenderer {
    override fun canRender(objectType: Int): Boolean {
        return EvaluationObjectType.fromInt(objectType) == EvaluationObjectType.CHECKBOX
    }

    @Composable
    override fun Render(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val selectedValues = if (currentAnswer.isEmpty()) {
            emptyList()
        } else {
            currentAnswer.split(",").map { it.trim() }
        }

        val hasStyle = question.style.isNotEmpty()

        if (hasStyle) {
            val options = question.available_values.map {
                CheckboxOption(
                    text = it.available_value,
                    isChecked = selectedValues.contains(it.available_value)
                )
            }

            DmtCheckboxCardGroup(
                options = options,
                onCheckedChange = { selected ->
                    onAnswerChange(selected.joinToString(","))
                },
                allowMultiple = true
            )
        } else {
            val options = question.available_values?.map { it.available_value } ?: emptyList()

            DmtCheckBoxGroup(
                items = options,
                selectedItems = selectedValues,
                onSelectionChange = { selected ->
                    onAnswerChange(selected.joinToString(","))
                }
            )
        }
    }
}