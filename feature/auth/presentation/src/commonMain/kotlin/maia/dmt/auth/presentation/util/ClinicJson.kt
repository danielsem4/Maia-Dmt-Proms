package maia.dmt.auth.presentation.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import maia.dmt.core.domain.dto.Clinic

@Serializable
internal data class ClinicJson(
    val id: String,
    val clinicName: String,
    val clinicUrl: String = "",
    val clinicImageUrl: String? = null,
    val isResearchClinic: Boolean = false
)

private val json = Json { ignoreUnknownKeys = true }

fun List<Clinic>.toJsonString(): String {
    return json.encodeToString(map {
        ClinicJson(
            id = it.id,
            clinicName = it.clinicName,
            clinicUrl = it.clinicUrl,
            clinicImageUrl = it.clinicImageUrl,
            isResearchClinic = it.isResearchClinic
        )
    })
}

fun String.toClinics(): List<Clinic> {
    return try {
        json.decodeFromString<List<ClinicJson>>(this).map {
            Clinic(
                id = it.id,
                clinicName = it.clinicName,
                clinicUrl = it.clinicUrl,
                clinicImageUrl = it.clinicImageUrl,
                isResearchClinic = it.isResearchClinic
            )
        }
    } catch (e: Exception) {
        emptyList()
    }
}
