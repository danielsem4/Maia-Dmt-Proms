package maia.dmt.core.domain.dto

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

sealed class MeasurementDetail {
    abstract val dateTime: String
    abstract val measureObject: Int
}


data class MeasurementDetailString(
    override val dateTime: String,
    override val measureObject: Int,
    val value: String
) : MeasurementDetail()


data class MeasurementDetailInt(
    override val dateTime: String,
    override val measureObject: Int,
    val value: Int
) : MeasurementDetail()


data class MeasurementDetailGeneric(
    override val dateTime: String,
    override val measureObject: Int,
    val value: JsonElement
) : MeasurementDetail()


