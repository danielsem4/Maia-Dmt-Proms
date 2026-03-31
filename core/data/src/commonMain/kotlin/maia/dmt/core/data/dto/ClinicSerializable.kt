package maia.dmt.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClinicSerializable(
    val id: String = "",
    val clinic_name: String = "",
    val clinic_url: String = "",
    val clinic_image_url: String? = null,
    val is_research_clinic: Boolean = false
)
