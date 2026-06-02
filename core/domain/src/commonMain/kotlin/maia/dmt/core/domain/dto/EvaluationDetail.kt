package maia.dmt.core.domain.dto

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

sealed class EvaluationDetail {
    abstract val dateTime: String
    abstract val evaluationObject: Int
}


data class EvaluationDetailString(
    override val dateTime: String,
    override val evaluationObject: Int,
    val value: String
) : EvaluationDetail()


data class EvaluationDetailInt(
    override val dateTime: String,
    override val evaluationObject: Int,
    val value: Int
) : EvaluationDetail()


data class EvaluationDetailGeneric(
    override val dateTime: String,
    override val evaluationObject: Int,
    val value: JsonElement
) : EvaluationDetail()


