package maia.dmt.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginSuccessfulRequestSerializable(
    val tokens: AuthTokensSerializable? = null,
    val user: UserSerializable? = UserSerializable(),
)
