package maia.dmt.evaluation.presentation.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import org.jetbrains.compose.ui.tooling.preview.Preview

object QuestionRendererProvider {

    private val renderers: List<QuestionRenderer> = listOf(
        InputQuestionRenderer(),
        RadioQuestionRenderer(),
        CheckboxQuestionRenderer(),
        ScaleQuestionRenderer(),
        BodyQuestionRenderer(),
    )

    @Composable
    fun RenderQuestion(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val renderer = renderers.firstOrNull { it.canRender(question.object_type) }

        renderer?.Render(
            question = question,
            currentAnswer = currentAnswer,
            onAnswerChange = onAnswerChange
        ) ?: run {
            UnsupportedQuestionRenderer().Render(
                question = question,
                currentAnswer = currentAnswer,
                onAnswerChange = onAnswerChange
            )
        }
    }
}

@Composable
@Preview
fun QuestionRendererPreview() {
    DmtTheme {  }
}