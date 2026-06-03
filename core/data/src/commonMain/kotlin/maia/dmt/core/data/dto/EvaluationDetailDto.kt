package maia.dmt.core.data.dto

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

@Serializable(with = EvaluationDetailSerializer::class)
sealed class EvaluationDetailDto {
    abstract val dateTime: String
    abstract val evaluationObject: Int
}


@Serializable
data class EvaluationDetailStringDto(
    @SerialName("DateTime")
    override val dateTime: String,
    override val evaluationObject: Int,
    val value: String
) : EvaluationDetailDto()


@Serializable
data class EvaluationDetailIntDto(
    @SerialName("DateTime")
    override val dateTime: String,
    override val evaluationObject: Int,
    val value: Int
) : EvaluationDetailDto()


@Serializable
data class EvaluationDetailGenericDto(
    @SerialName("DateTime")
    override val dateTime: String,
    override val evaluationObject: Int,
    val value: JsonElement
) : EvaluationDetailDto()


object EvaluationDetailSerializer : JsonContentPolymorphicSerializer<EvaluationDetailDto>(EvaluationDetailDto::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out EvaluationDetailDto> {
        val jsonObject = element.jsonObject
        val valueElement = jsonObject["value"]
            ?: throw SerializationException("The 'value' key is missing in an evaluation detail object.")

        return when {
            valueElement is JsonPrimitive && valueElement.isString -> EvaluationDetailStringDto.serializer()
            valueElement is JsonPrimitive -> EvaluationDetailIntDto.serializer()
            else -> EvaluationDetailGenericDto.serializer()
        }
    }
}

