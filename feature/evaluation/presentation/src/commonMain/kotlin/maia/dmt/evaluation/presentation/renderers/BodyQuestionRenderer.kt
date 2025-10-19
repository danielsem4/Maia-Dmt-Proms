package maia.dmt.evaluation.presentation.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.select.DmtHumanBodyLayout
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.evaluation.presentation.evaluation.EvaluationObjectType

class BodyQuestionRenderer : QuestionRenderer {
    override fun canRender(objectType: Int): Boolean {
        return EvaluationObjectType.fromInt(objectType) == EvaluationObjectType.BODY
    }

    @Composable
    override fun Render(
        question: EvaluationObject,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val frontAreaNames = setOf(
            "Head", "Chest", "Belly",
            "Left Shoulder", "Right Shoulder",
            "Left Arm", "Right Arm",
            "Left Knee", "Right Knee",
            "Left Foot", "Right Foot"
        )

        val backAreaNames = setOf(
            "Neck", "Upper Back", "Lower Back", "Buttocks"
        )

        val frontValues = mutableListOf<String>()
        val backValues = mutableListOf<String>()

        question.available_values.forEach { evaluationValue ->
            val availableValue = evaluationValue.available_value

            val parts = availableValue.split("(?)")
            if (parts.size >= 2) {
                val areaName = parts[0].trim()
                val painOptions = parts[1].trim()

                // Check if this area belongs to front or back
                when {
                    frontAreaNames.any { areaName.contains(it, ignoreCase = true) } -> {
                        frontValues.add(availableValue)
                    }
                    backAreaNames.any { areaName.contains(it, ignoreCase = true) } -> {
                        backValues.add(availableValue)
                    }
                }
            }
        }

        val initialSelections = parseCurrentAnswer(currentAnswer)

        DmtHumanBodyLayout(
            frontAreasValues = frontValues,
            backAreasValues = backValues,
            initialSelections = initialSelections,
            onPainAreasChanged = { painAreas ->
                val formatted = painAreas.entries.joinToString(";") { (area, pains) ->
                    "$area:${pains.joinToString(",")}"
                }
                onAnswerChange(formatted)
            }
        )
    }

    private fun parseCurrentAnswer(answer: String): Map<String, List<String>> {
        if (answer.isBlank()) return emptyMap()

        return answer.split(";").mapNotNull { entry ->
            val parts = entry.split(":")
            if (parts.size == 2) {
                val area = parts[0]
                val pains = parts[1].split(",").filter { it.isNotBlank() }
                area to pains
            } else null
        }.toMap()
    }
}