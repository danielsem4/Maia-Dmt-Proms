package maia.dmt.core.designsystem.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.buttons.DmtToggleButton
import maia.dmt.core.designsystem.components.buttons.ToggleOrientation
import maia.dmt.core.designsystem.components.select.CheckboxOption
import maia.dmt.core.designsystem.components.select.DmtCheckboxCardGroup
import maia.dmt.core.designsystem.components.select.DmtRadioButtonGroup
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.core.domain.dto.evaluation.EvaluationObjectType

class RadioQuestionRenderer : QuestionRenderer {
    override fun canRender(objectType: Int): Boolean {
        return EvaluationObjectType.fromInt(objectType) == EvaluationObjectType.RADIO
    }

    @Composable
    override fun Render(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val hasStyle = question.style?.isNotEmpty() == true

        if (hasStyle) {
            when (question.style) {
                "none" -> {

                }

                "toggle button" -> {
                    val texts = question.available_values?.map { it.available_value } ?: emptyList()
                    val selectedIndex = texts.indexOf(currentAnswer).takeIf { it >= 0 } ?: -1

                    DmtToggleButton(
                        texts = texts,
                        selectedIndex = selectedIndex,
                        onSelectionChange = { index ->
                            if (index in texts.indices) {
                                onAnswerChange(texts[index])
                            }
                        },
                        orientation = ToggleOrientation.Row
                    )
                }

                "radio button" -> {
                    val options = question.available_values?.map {
                        CheckboxOption(
                            text = it.available_value,
                            isChecked = it.available_value == currentAnswer
                        )
                    } ?: emptyList()

                    DmtCheckboxCardGroup(
                        options = options,
                        onCheckedChange = { selected ->
                            onAnswerChange(selected.firstOrNull() ?: "")
                        },
                        allowMultiple = false
                    )
                }

                else -> {
                    DefaultRadioButtons(question, currentAnswer, onAnswerChange)
                }
            }
        } else {
            DefaultRadioButtons(question, currentAnswer, onAnswerChange)
        }
    }

    @Composable
    private fun DefaultRadioButtons(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val options = question.available_values?.map { it.available_value } ?: emptyList()

        DmtRadioButtonGroup(
            items = options,
            selectedItem = currentAnswer.ifEmpty { null },
            onSelectionChange = onAnswerChange
        )
    }
}