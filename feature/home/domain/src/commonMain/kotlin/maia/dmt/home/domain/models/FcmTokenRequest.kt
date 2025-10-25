package maia.dmt.home.domain.models

data class FcmTokenRequest(
    val user_id: String,
    val clinic_id: String,
    val fcm_token: String
)
