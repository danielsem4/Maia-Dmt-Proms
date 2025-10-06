package maia.dmt.core.data.dto

import kotlinx.serialization.Serializable
import maia.dmt.core.domain.dto.User

@Serializable
data class LoginSuccessfulRequestSerializable(
    val token: String? = null,
    val user: UserSerializable? = UserSerializable(),
)
