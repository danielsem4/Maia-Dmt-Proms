package maia.dmt.core.domain.evaluation

data class EvaluationStructure(
    val evaluationId: String,
    val evaluationName: String,
    val screens: List<EvaluationScreen>
)

data class EvaluationScreen(
    val id: String,
    val screenNumber: Int,
    val title: String,
    val rows: List<EvaluationRow>
)

data class EvaluationRow(
    val rowNumber: Int,
    val elements: List<EvaluationElement>
)

data class EvaluationElement(
    val id: String,
    val elementType: ElementType,
    val rowNumber: Int,
    val orderInRow: Int,
    val label: String,
    val isRequired: Boolean,
    val config: ElementConfig
)

enum class ElementType {
    HEADER,
    PARAGRAPH,
    INPUT_TEXT,
    INPUT_RADIO,
    INPUT_MULTI_SELECT,
    INPUT_SELECT,
    INPUT_SCALE,
    UNKNOWN;

    companion object {
        fun fromString(value: String): ElementType {
            return entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}

sealed interface ElementConfig {
    data object EmptyConfig : ElementConfig

    data class InputTextConfig(
        val placeholder: String
    ) : ElementConfig

    data class InputRadioConfig(
        val options: List<String>,
        val layout: String,
        val displayStyle: String
    ) : ElementConfig

    data class InputMultiSelectConfig(
        val options: List<String>,
        val layout: String,
        val displayStyle: String
    ) : ElementConfig

    data class InputSelectConfig(
        val options: List<String>,
        val placeholder: String
    ) : ElementConfig

    data class InputScaleConfig(
        val min: Int,
        val max: Int,
        val step: Int,
        val minLabel: String,
        val maxLabel: String
    ) : ElementConfig
}
