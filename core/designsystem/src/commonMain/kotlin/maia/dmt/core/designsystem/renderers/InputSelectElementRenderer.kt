package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import maia.dmt.core.designsystem.components.select.DmtDropDown
import maia.dmt.core.domain.measurement.ElementConfig
import maia.dmt.core.domain.measurement.ElementType
import maia.dmt.core.domain.measurement.MeasurementElement

class InputSelectElementRenderer : ElementRenderer {
    override fun canRender(elementType: ElementType): Boolean {
        return elementType == ElementType.INPUT_SELECT
    }

    @Composable
    override fun Render(
        element: MeasurementElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val config = element.config as? ElementConfig.InputSelectConfig ?: return
        val options = config.options

        DmtDropDown(
            items = options,
            selectedItem = currentAnswer.ifEmpty { null },
            onItemSelected = { onAnswerChange(it) },
            placeholder = config.placeholder,
            modifier = Modifier.fillMaxWidth(),
            itemContent = { Text(it) }
        )
    }
}
