package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import maia.dmt.core.designsystem.components.textFields.DmtTextField
import maia.dmt.core.domain.evaluation.ElementType
import maia.dmt.core.domain.evaluation.EvaluationElement

class CognitiveFieldElementRenderer : ElementRenderer {
    override fun canRender(elementType: ElementType): Boolean {
        return elementType == ElementType.COGNITIVE_FIELD
    }

    @Composable
    override fun Render(
        element: EvaluationElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val state = rememberTextFieldState(initialText = currentAnswer)

        val currentText = state.text.toString()
        LaunchedEffect(currentText) {
            if (currentText != currentAnswer) {
                onAnswerChange(currentText)
            }
        }

        LaunchedEffect(currentAnswer) {
            if (currentAnswer != state.text.toString()) {
                state.setTextAndPlaceCursorAtEnd(currentAnswer)
            }
        }

        DmtTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}
