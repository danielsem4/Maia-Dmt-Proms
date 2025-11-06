package maia.dmt.core.designsystem.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.core.domain.dto.evaluation.EvaluationObjectType

class DropDownQuestionRenderer : QuestionRenderer {
    override fun canRender(objectType: Int): Boolean {
        return EvaluationObjectType.fromInt(objectType) == EvaluationObjectType.DROPDOWN
    }

    @Composable
    override fun Render(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {

    }
}