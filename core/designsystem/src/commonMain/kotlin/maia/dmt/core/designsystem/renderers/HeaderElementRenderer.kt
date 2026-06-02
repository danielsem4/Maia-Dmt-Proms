package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import maia.dmt.core.domain.evaluation.ElementType
import maia.dmt.core.domain.evaluation.EvaluationElement

class HeaderElementRenderer : ElementRenderer {
    override fun canRender(elementType: ElementType): Boolean {
        return elementType == ElementType.HEADER
    }

    @Composable
    override fun Render(
        element: EvaluationElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = element.label,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}
