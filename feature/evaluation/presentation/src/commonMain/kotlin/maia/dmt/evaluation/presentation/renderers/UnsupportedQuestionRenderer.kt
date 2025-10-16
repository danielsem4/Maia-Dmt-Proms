package maia.dmt.evaluation.presentation.renderers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maia.dmt.core.domain.dto.evaluation.EvaluationObject

class UnsupportedQuestionRenderer : QuestionRenderer {
    override fun canRender(objectType: Int): Boolean = true

    @Composable
    override fun Render(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Question type ${question.object_type} is not yet supported",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}