package maia.dmt.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refresh: String
)
