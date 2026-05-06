package maia.dmt.core.data.dto.measurement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class MeasurementStructureDto(
    @SerialName("measurement_id")
    val measurementId: String,
    @SerialName("measurement_name")
    val measurementName: String,
    val screens: List<MeasurementScreenDto>
)

@Serializable
data class MeasurementScreenDto(
    val id: String,
    @SerialName("screen_number")
    val screenNumber: Int,
    val title: String,
    val rows: List<MeasurementRowDto>
)

@Serializable
data class MeasurementRowDto(
    @SerialName("row_number")
    val rowNumber: Int,
    val elements: List<MeasurementElementDto>
)

@Serializable
data class MeasurementElementDto(
    val id: String,
    @SerialName("element_type")
    val elementType: String,
    @SerialName("row_number")
    val rowNumber: Int,
    @SerialName("order_in_row")
    val orderInRow: Int,
    val label: String,
    @SerialName("is_required")
    val isRequired: Boolean,
    val config: JsonObject = JsonObject(emptyMap())
)
