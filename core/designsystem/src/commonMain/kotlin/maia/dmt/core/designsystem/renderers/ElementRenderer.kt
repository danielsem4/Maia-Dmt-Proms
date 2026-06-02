package maia.dmt.core.designsystem.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.domain.evaluation.ElementType
import maia.dmt.core.domain.evaluation.EvaluationElement

interface ElementRenderer {
    fun canRender(elementType: ElementType): Boolean

    @Composable
    fun Render(
        element: EvaluationElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    )
}
