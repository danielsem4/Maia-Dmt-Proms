package maia.dmt.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokensSerializable(
    val access: String = "",
    val refresh: String = ""
)
