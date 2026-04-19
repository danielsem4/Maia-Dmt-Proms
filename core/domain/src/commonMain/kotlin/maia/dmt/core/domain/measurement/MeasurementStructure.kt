package maia.dmt.core.domain.measurement

data class MeasurementStructure(
    val measurementId: String,
    val measurementName: String,
    val screens: List<MeasurementScreen>
)

data class MeasurementScreen(
    val id: String,
    val screenNumber: Int,
    val title: String,
    val rows: List<MeasurementRow>
)

data class MeasurementRow(
    val rowNumber: Int,
    val elements: List<MeasurementElement>
)

data class MeasurementElement(
    val id: String,
    val elementType: String,
    val rowNumber: Int,
    val orderInRow: Int,
    val label: String,
    val isRequired: Boolean,
    val config: String
)
