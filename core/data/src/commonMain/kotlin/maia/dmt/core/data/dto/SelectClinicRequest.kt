package maia.dmt.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SelectClinicRequest(
    val clinic_id: String,
    val user_id: String
)
