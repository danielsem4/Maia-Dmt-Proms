package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import maia.dmt.core.designsystem.components.textFields.DmtParagraphTextField
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.core.domain.dto.evaluation.EvaluationObjectType

class InputQuestionRenderer : QuestionRenderer {
    override fun canRender(objectType: Int): Boolean {
        return EvaluationObjectType.fromInt(objectType) == EvaluationObjectType.INPUT
    }

    @Composable
    override fun Render(
        question: EvaluationObject,
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
                state.setTextAndPlaceCursorAtEnd(currentText)
            }
        }

        DmtParagraphTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            placeholder = question.object_label
        )
    }
}