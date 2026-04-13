package maia.dmt.core.data.auth

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object JwtDecoder {

    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalEncodingApi::class)
    fun getActiveClinicId(accessToken: String): String? {
        return try {
            val parts = accessToken.split(".")
            if (parts.size < 2) return null
            val payload = parts[1]
            // JWT Base64URL may need padding
            val padded = when (payload.length % 4) {
                2 -> "$payload=="
                3 -> "$payload="
                else -> payload
            }
            val decoded = Base64.UrlSafe.decode(padded).decodeToString()
            val jsonElement = json.parseToJsonElement(decoded)
            jsonElement.jsonObject["active_clinic_id"]?.jsonPrimitive?.content
        } catch (e: Exception) {
            null
        }
    }
}
