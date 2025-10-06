package maia.dmt.core.domain.auth

import kotlinx.coroutines.flow.Flow
import maia.dmt.core.domain.dto.LoginSuccessfulRequest

interface SessionStorage {
    fun observeAuthInfo(): Flow<LoginSuccessfulRequest?>
    suspend fun set(info: LoginSuccessfulRequest?)
}