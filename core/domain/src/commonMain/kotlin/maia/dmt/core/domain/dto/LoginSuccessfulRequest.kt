package maia.dmt.core.domain.dto

data class LoginSuccessfulRequest(
    val token: String? = null,
    val user: User? = User(),
)