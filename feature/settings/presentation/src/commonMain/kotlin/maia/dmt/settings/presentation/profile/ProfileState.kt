package maia.dmt.settings.presentation.profile

import maia.dmt.core.domain.dto.User

data class ProfileState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)