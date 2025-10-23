package maia.dmt.proms

import maia.dmt.core.domain.dto.User

data class MainState(
    val isLoggedIn: Boolean = false,
    val isCheckingAuth: Boolean = true,
    val user: User? = null
)
