package maia.dmt.evaluation.presentation.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.select.DmtHumanBodyLayout
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.evaluation.presentation.evaluation.EvaluationObjectType

class BodyQuestionRenderer : QuestionRenderer {
    override fun canRender(objectType: Int): Boolean {
        return EvaluationObjectType.fromInt(objectType) == EvaluationObjectType.BODY
    }

    @Composable
    override fun Render(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        DmtHumanBodyLayout(
            onPainAreasChanged = { painAreas ->
                val formatted = painAreas.entries.joinToString(";") { (area, pains) ->
                    "$area:${pains.joinToString(",")}"
                }
                onAnswerChange(formatted)
            }
        )
    }
}
