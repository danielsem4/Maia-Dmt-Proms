package maia.dmt.evaluation.presentation.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.domain.dto.evaluation.EvaluationObject

interface QuestionRenderer {
    fun canRender(objectType: Int): Boolean

    @Composable
    fun Render(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    )
}
