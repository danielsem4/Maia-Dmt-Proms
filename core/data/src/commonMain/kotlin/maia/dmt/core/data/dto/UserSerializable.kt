package maia.dmt.core.data.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

object ModulesSerializer : KSerializer<List<ModuleSerializable>> {
    private val delegateSerializer = ListSerializer(JsonArray.serializer())

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Modules")

    override fun serialize(encoder: Encoder, value: List<ModuleSerializable>) {
        // Not needed for your use case, but here for completeness
        val jsonEncoder = encoder as JsonEncoder
        val jsonArray = buildJsonArray {
            value.forEach { module ->
                add(buildJsonArray {
                    add(module.module_name)
                    add(module.module_id)
                })
            }
        }
        jsonEncoder.encodeJsonElement(jsonArray)
    }

    override fun deserialize(decoder: Decoder): List<ModuleSerializable> {
        val jsonDecoder = decoder as JsonDecoder
        val jsonArray = jsonDecoder.decodeJsonElement().jsonArray

        return jsonArray.map { element ->
            val array = element.jsonArray
            ModuleSerializable(
                module_name = array[0].jsonPrimitive.content,
                module_id = array[1].jsonPrimitive.int
            )
        }
    }
}


@Serializable
data class UserSerializable(
    val id: Int = 1,
    val password: String? = null,
    val last_login: String? = null,
    val is_superuser: Boolean? = null,
    val is_staff: Boolean? = null,
    val is_active: Boolean? = null,
    val date_joined: String? = null,
    val email: String? = null,
    val phone_number: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val is_clinic_manager: Boolean? = null,
    val is_doctor: Boolean? = null,
    val is_patient: Boolean? = null,
    val is_research_patient: Boolean? = null,
    val groups: List<String> = emptyList(),
    val user_permissions: List<String> = emptyList(),
    val clinicId: Int = 1,
    val clinicName: String? = null,
    @Serializable(with = ModulesSerializer::class)
    var modules: List<ModuleSerializable> = emptyList(),
    val status: String? = null,
    val server_url: String? = "null",
    val hospital_image: String? = ""
)
