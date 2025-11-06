package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.core.domain.dto.evaluation.EvaluationObjectType

class InstructionsQuestionRenderer : QuestionRenderer {
    override fun canRender(objectType: Int): Boolean {
        return EvaluationObjectType.fromInt(objectType) == EvaluationObjectType.INSTRUCTION
    }

    @Composable
    override fun Render(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = question.object_label,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}