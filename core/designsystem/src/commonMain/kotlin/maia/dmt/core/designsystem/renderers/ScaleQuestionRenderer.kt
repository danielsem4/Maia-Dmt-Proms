package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import maia.dmt.core.designsystem.components.scale.DmtScaleSlider
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.core.domain.dto.evaluation.EvaluationObjectType

class ScaleQuestionRenderer : QuestionRenderer {
    override fun canRender(objectType: Int): Boolean {
        return EvaluationObjectType.fromInt(objectType) == EvaluationObjectType.SCALE
    }

    @Composable
    override fun Render(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val values = question.available_values ?: emptyList()

        if (values.size < 3) {
            // Invalid scale configuration
            return
        }

        // Parse start value: "1(?) no pain" -> [1, "no pain"]
        val startParts = values[0].available_value.split("(?)")
        val startValue = startParts[0].trim().toIntOrNull() ?: 1
        val startText = startParts.getOrNull(1)?.trim() ?: ""

        // Parse end value: "10(?) pain" -> [10, "pain"]
        val endParts = values[1].available_value.split("(?)")
        val endValue = endParts[0].trim().toIntOrNull() ?: 10
        val endText = endParts.getOrNull(1)?.trim() ?: ""

        // Parse step: "1" -> 1
        val step = values[2].available_value.trim().toIntOrNull() ?: 1

        DmtScaleSlider(
            startValue = startValue,
            endValue = endValue,
            step = step,
            startText = startText,
            endText = endText,
            initialValue = currentAnswer.toIntOrNull() ?: startValue,
            onValueChange = { onAnswerChange(it.toString()) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}