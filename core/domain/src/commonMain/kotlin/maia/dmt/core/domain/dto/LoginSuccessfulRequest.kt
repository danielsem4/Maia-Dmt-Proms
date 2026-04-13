package maia.dmt.core.domain.dto

data class LoginSuccessfulRequest(
    val tokens: AuthTokens? = null,
    val user: User? = User(),
)