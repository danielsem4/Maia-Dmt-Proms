package maia.dmt.core.domain.dto

data class Clinic(
    val id: String,
    val clinicName: String,
    val clinicUrl: String = "",
    val clinicImageUrl: String? = null,
    val isResearchClinic: Boolean = false
)
