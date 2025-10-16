package maia.dmt.evaluation.presentation.renderers

import androidx.compose.runtime.Composable
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
        val options = question.available_values?.map { it.available_value } ?: emptyList()

        DmtRadioButtonGroup(
            items = options,
            selectedItem = currentAnswer.ifEmpty { null },
            onSelectionChange = onAnswerChange
        )
    }
}