package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import maia.dmt.core.designsystem.components.scale.DmtScaleSlider
import maia.dmt.core.domain.measurement.ElementConfig
import maia.dmt.core.domain.measurement.ElementType
import maia.dmt.core.domain.measurement.MeasurementElement

class InputScaleElementRenderer : ElementRenderer {
    override fun canRender(elementType: ElementType): Boolean {
        return elementType == ElementType.INPUT_SCALE
    }

    @Composable
    override fun Render(
        element: MeasurementElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val config = element.config as? ElementConfig.InputScaleConfig ?: return

        DmtScaleSlider(
            startValue = config.min,
            endValue = config.max,
            step = config.step,
            startText = config.minLabel,
            endText = config.maxLabel,
            initialValue = currentAnswer.toIntOrNull() ?: config.min,
            onValueChange = { onAnswerChange(it.toString()) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
