package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maia.dmt.core.domain.measurement.MeasurementElement

object ElementRendererProvider {

    private val renderers: List<ElementRenderer> = listOf(
        HeaderElementRenderer(),
        ParagraphElementRenderer(),
        InputTextElementRenderer(),
        InputRadioElementRenderer(),
        InputMultiSelectElementRenderer(),
        InputSelectElementRenderer(),
        InputScaleElementRenderer()
    )

    @Composable
    fun RenderElement(
        element: MeasurementElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val renderer = renderers.firstOrNull { it.canRender(element.elementType) }

        if (renderer != null) {
            renderer.Render(
                element = element,
                currentAnswer = currentAnswer,
                onAnswerChange = onAnswerChange
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Element type ${element.elementType} is not yet supported",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
