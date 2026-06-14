package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import maia.dmt.core.designsystem.components.textFields.DmtTextField
import maia.dmt.core.domain.evaluation.ElementConfig
import maia.dmt.core.domain.evaluation.ElementType
import maia.dmt.core.domain.evaluation.EvaluationElement

class InputNumberElementRenderer : ElementRenderer {
    override fun canRender(elementType: ElementType): Boolean {
        return elementType == ElementType.INPUT_NUMBER
    }

    @Composable
    override fun Render(
        element: EvaluationElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val config = element.config as? ElementConfig.InputNumberConfig
        val placeholder = config?.placeholder ?: ""

        val state = rememberTextFieldState(initialText = currentAnswer)

        val currentText = state.text.toString()
        LaunchedEffect(currentText) {
            val digitsOnly = currentText.filter { it.isDigit() || it == '-' }
            if (digitsOnly != currentText) {
                state.setTextAndPlaceCursorAtEnd(digitsOnly)
                return@LaunchedEffect
            }
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
            placeholder = placeholder,
            singleLine = true,
            keyboardType = KeyboardType.Number
        )
    }
}
