package maia.dmt.core.data.dto.evaluation

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull


@Serializable
data class EvaluationSettingsDto(
    val id: Int,
    val evaluation_repeat_period: String,
    val evaluation_repeat_times: Double?,
    val evaluation_begin_time: String,
    val evaluation_last_time: String?,
    val evaluation_end_time: String,
    val is_repetitive: Boolean,
    val times_taken: Int,
    val patient: Int,
    val doctor: Int,
    val clinic: Int,
    val evaluation: Int
)