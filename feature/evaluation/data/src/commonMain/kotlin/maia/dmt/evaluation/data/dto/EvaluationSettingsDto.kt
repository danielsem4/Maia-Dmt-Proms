package maia.dmt.evaluation.data.dto

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
    val measurement_repeat_period: String,
    @Serializable(with = DoubleToIntSerializer::class)
    val measurement_repeat_times: Int,
    val measurement_begin_time: String,
    val measurement_last_time: String,
    val measurement_end_time: String,
    val is_repetitive: Boolean,
    val times_taken: Int,
    val patient: Int,
    val doctor: Int,
    val clinic: Int,
    val measurement: Int
)

object DoubleToIntSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DoubleToInt", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Int {
        val jsonDecoder = decoder as JsonDecoder
        val element = jsonDecoder.decodeJsonElement()

        return when (element) {
            is JsonPrimitive -> {
                element.intOrNull ?: element.doubleOrNull?.toInt()
                ?: throw IllegalArgumentException("Cannot convert to Int")
            }
            else -> throw IllegalArgumentException("Expected a number")
        }
    }

    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeInt(value)
    }
}
