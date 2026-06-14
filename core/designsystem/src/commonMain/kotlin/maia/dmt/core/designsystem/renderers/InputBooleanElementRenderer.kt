package maia.dmt.core.designsystem.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.buttons.DmtToggleButton
import maia.dmt.core.designsystem.components.buttons.ToggleOrientation
import maia.dmt.core.domain.evaluation.ElementConfig
import maia.dmt.core.domain.evaluation.ElementType
import maia.dmt.core.domain.evaluation.EvaluationElement

class InputBooleanElementRenderer : ElementRenderer {
    override fun canRender(elementType: ElementType): Boolean {
        return elementType == ElementType.INPUT_BOOLEAN
    }

    @Composable
    override fun Render(
        element: EvaluationElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val config = element.config as? ElementConfig.InputBooleanConfig ?: return

        val texts = listOf(config.falseLabel, config.trueLabel)
        val selectedIndex = when (currentAnswer) {
            "true" -> 1
            "false" -> 0
            else -> -1
        }

        DmtToggleButton(
            texts = texts,
            selectedIndex = selectedIndex,
            onSelectionChange = { index ->
                onAnswerChange(if (index == 1) "true" else "false")
            },
            orientation = ToggleOrientation.Row
        )
    }
}
