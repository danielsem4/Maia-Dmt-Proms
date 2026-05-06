package maia.dmt.core.designsystem.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.select.CheckboxOption
import maia.dmt.core.designsystem.components.select.DmtCheckBoxGroup
import maia.dmt.core.designsystem.components.select.DmtCheckboxCardGroup
import maia.dmt.core.domain.measurement.ElementConfig
import maia.dmt.core.domain.measurement.ElementType
import maia.dmt.core.domain.measurement.MeasurementElement

class InputMultiSelectElementRenderer : ElementRenderer {
    override fun canRender(elementType: ElementType): Boolean {
        return elementType == ElementType.INPUT_MULTI_SELECT
    }

    @Composable
    override fun Render(
        element: MeasurementElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val config = element.config as? ElementConfig.InputMultiSelectConfig ?: return
        val options = config.options
        val selectedValues = if (currentAnswer.isEmpty()) {
            emptyList()
        } else {
            currentAnswer.split(",").map { it.trim() }
        }

        when (config.displayStyle) {
            "cards" -> {
                val cardOptions = options.map {
                    CheckboxOption(
                        text = it,
                        isChecked = selectedValues.contains(it)
                    )
                }
                DmtCheckboxCardGroup(
                    options = cardOptions,
                    onCheckedChange = { selected ->
                        onAnswerChange(selected.joinToString(","))
                    },
                    allowMultiple = true
                )
            }
            else -> {
                DmtCheckBoxGroup(
                    items = options,
                    selectedItems = selectedValues,
                    onSelectionChange = { selected ->
                        onAnswerChange(selected.joinToString(","))
                    }
                )
            }
        }
    }
}
