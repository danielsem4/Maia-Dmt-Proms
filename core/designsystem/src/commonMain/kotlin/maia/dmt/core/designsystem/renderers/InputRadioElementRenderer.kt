package maia.dmt.core.designsystem.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.buttons.DmtToggleButton
import maia.dmt.core.designsystem.components.buttons.ToggleOrientation
import maia.dmt.core.designsystem.components.select.CheckboxOption
import maia.dmt.core.designsystem.components.select.DmtCheckboxCardGroup
import maia.dmt.core.designsystem.components.select.DmtRadioButtonGroup
import maia.dmt.core.domain.measurement.ElementConfig
import maia.dmt.core.domain.measurement.ElementType
import maia.dmt.core.domain.measurement.MeasurementElement

class InputRadioElementRenderer : ElementRenderer {
    override fun canRender(elementType: ElementType): Boolean {
        return elementType == ElementType.INPUT_RADIO
    }

    @Composable
    override fun Render(
        element: MeasurementElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val config = element.config as? ElementConfig.InputRadioConfig ?: return
        val options = config.options

        when {
            config.displayStyle == "cards" -> {
                val cardOptions = options.map {
                    CheckboxOption(
                        text = it,
                        isChecked = it == currentAnswer
                    )
                }
                DmtCheckboxCardGroup(
                    options = cardOptions,
                    onCheckedChange = { selected ->
                        onAnswerChange(selected.firstOrNull() ?: "")
                    },
                    allowMultiple = false
                )
            }
            config.layout == "horizontal" -> {
                val selectedIndex = options.indexOf(currentAnswer).takeIf { it >= 0 } ?: -1
                DmtToggleButton(
                    texts = options,
                    selectedIndex = selectedIndex,
                    onSelectionChange = { index ->
                        if (index in options.indices) {
                            onAnswerChange(options[index])
                        }
                    },
                    orientation = ToggleOrientation.Row
                )
            }
            else -> {
                DmtRadioButtonGroup(
                    items = options,
                    selectedItem = currentAnswer.ifEmpty { null },
                    onSelectionChange = onAnswerChange
                )
            }
        }
    }
}
