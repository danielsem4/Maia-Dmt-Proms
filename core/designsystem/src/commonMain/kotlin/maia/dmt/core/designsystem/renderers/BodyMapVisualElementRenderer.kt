package maia.dmt.core.designsystem.renderers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import maia.dmt.core.designsystem.components.select.DmtHumanBodyLayout
import maia.dmt.core.domain.evaluation.ElementConfig
import maia.dmt.core.domain.evaluation.ElementType
import maia.dmt.core.domain.evaluation.EvaluationElement

class BodyMapVisualElementRenderer : ElementRenderer {
    override fun canRender(elementType: ElementType): Boolean {
        return elementType == ElementType.BODY_MAP_VISUAL
    }

    @Composable
    override fun Render(
        element: EvaluationElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    ) {
        val config = element.config as? ElementConfig.BodyMapVisualConfig ?: return

        val frontValues = config.spots
            .filter { it.point.lowercase() in FRONT_POINTS }
            .map { "${toAreaId(it.point)} (?) ${it.subItems.joinToString(", ")}" }

        val backValues = config.spots
            .filter { it.point.lowercase() in BACK_POINTS }
            .map { "${toAreaId(it.point)} (?) ${it.subItems.joinToString(", ")}" }

        val initialSelections = decodeAnswer(currentAnswer)

        DmtHumanBodyLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp),
            frontAreasValues = frontValues,
            backAreasValues = backValues,
            initialSelections = initialSelections,
            onPainAreasChanged = { selections ->
                onAnswerChange(encodeAnswer(selections))
            }
        )
    }

    private fun toAreaId(point: String): String {
        return point.split("_")
            .joinToString(" ") { part ->
                part.replaceFirstChar { it.uppercase() }
            }
    }

    private fun encodeAnswer(selections: Map<String, List<String>>): String {
        if (selections.isEmpty()) return ""
        return Json.encodeToString(
            MapSerializer(String.serializer(), ListSerializer(String.serializer())),
            selections
        )
    }

    private fun decodeAnswer(answer: String): Map<String, List<String>> {
        if (answer.isBlank()) return emptyMap()
        return runCatching {
            Json.decodeFromString(
                MapSerializer(String.serializer(), ListSerializer(String.serializer())),
                answer
            )
        }.getOrDefault(emptyMap())
    }

    companion object {
        private val FRONT_POINTS = setOf(
            "head", "chest", "belly", "abdomen",
            "left_shoulder", "right_shoulder",
            "left_arm", "right_arm",
            "left_hand", "right_hand",
            "left_thigh", "right_thigh",
            "left_knee", "right_knee",
            "left_foot", "right_foot"
        )

        private val BACK_POINTS = setOf(
            "neck",
            "upper_back", "lower_back",
            "buttocks", "left_buttock", "right_buttock",
            "left_calf", "right_calf",
            "left_heel", "right_heel"
        )
    }
}
