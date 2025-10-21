package maia.dmt.core.data.dto

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

@Serializable(with = MeasurementDetailSerializer::class)
sealed class MeasurementDetailDto {
    abstract val dateTime: String
    abstract val measureObject: Int
}


@Serializable
data class MeasurementDetailStringDto(
    @SerialName("DateTime")
    override val dateTime: String,
    override val measureObject: Int,
    val value: String
) : MeasurementDetailDto()


@Serializable
data class MeasurementDetailIntDto(
    @SerialName("DateTime")
    override val dateTime: String,
    override val measureObject: Int,
    val value: Int
) : MeasurementDetailDto()


@Serializable
data class MeasurementDetailGenericDto(
    @SerialName("DateTime")
    override val dateTime: String,
    override val measureObject: Int,
    val value: JsonElement
) : MeasurementDetailDto()


object MeasurementDetailSerializer : JsonContentPolymorphicSerializer<MeasurementDetailDto>(MeasurementDetailDto::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out MeasurementDetailDto> {
        val jsonObject = element.jsonObject
        val valueElement = jsonObject["value"]
            ?: throw SerializationException("The 'value' key is missing in a measurement detail object.")

        return when {
            valueElement is JsonPrimitive && valueElement.isString -> MeasurementDetailStringDto.serializer()
            valueElement is JsonPrimitive -> MeasurementDetailIntDto.serializer()
            else -> MeasurementDetailGenericDto.serializer()
        }
    }
}

