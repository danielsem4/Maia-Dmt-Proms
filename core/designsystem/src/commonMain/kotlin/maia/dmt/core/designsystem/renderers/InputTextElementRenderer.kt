package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import maia.dmt.core.designsystem.components.textFields.DmtParagraphTextField
import maia.dmt.core.domain.measurement.ElementConfig
import maia.dmt.core.domain.measurement.ElementType
import maia.dmt.core.domain.measurement.MeasurementElement

class InputTextElementRenderer : ElementRenderer {
    override fun canRender(elementType: ElementType): Boolean {
        return elementType == ElementType.INPUT_TEXT
    }

    @Composable
    override fun Render(
        element: MeasurementElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val config = element.config as? ElementConfig.InputTextConfig
        val placeholder = config?.placeholder ?: ""

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

        DmtParagraphTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder
        )
    }
}
