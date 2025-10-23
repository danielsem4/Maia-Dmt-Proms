package maia.dmt.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequestDto(
    val user_id: String,
    val clinic_id: String,
    val fcm_token: String
)
