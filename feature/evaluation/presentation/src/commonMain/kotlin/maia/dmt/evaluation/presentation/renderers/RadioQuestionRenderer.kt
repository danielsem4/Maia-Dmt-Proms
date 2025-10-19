package maia.dmt.evaluation.presentation.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.select.CheckboxOption
import maia.dmt.core.designsystem.components.select.DmtCheckboxCardGroup
import maia.dmt.core.designsystem.components.select.DmtRadioButtonGroup
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.evaluation.presentation.evaluation.EvaluationObjectType

class RadioQuestionRenderer : QuestionRenderer {
    override fun canRender(objectType: Int): Boolean {
        return EvaluationObjectType.fromInt(objectType) == EvaluationObjectType.RADIO
    }

    @Composable
    override fun Render(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val hasStyle = question.style.isNotEmpty()

        if (hasStyle) {
            val options = question.available_values?.map {
                CheckboxOption(
                    text = it.available_value,
                    isChecked = it.available_value == currentAnswer
                )
            } ?: emptyList()

            DmtCheckboxCardGroup(
                options = options,
                onCheckedChange = { selected ->
                    onAnswerChange(selected.firstOrNull() ?: "")
                },
                allowMultiple = false
            )
        } else {
            val options = question.available_values.map { it.available_value }

            DmtRadioButtonGroup(
                items = options,
                selectedItem = currentAnswer.ifEmpty { null },
                onSelectionChange = onAnswerChange
            )
        }
    }
}
