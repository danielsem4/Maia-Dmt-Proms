package maia.dmt.core.domain.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import maia.dmt.core.domain.dto.LoginSuccessfulRequest

interface SessionStorage {
    fun observeAuthInfo(): Flow<LoginSuccessfulRequest?>
    suspend fun set(info: LoginSuccessfulRequest?, activeClinicId: String? = null)
    suspend fun getActiveClinicId(): String?
    suspend fun setActiveClinicId(clinicId: String?)

    suspend fun getAccessToken(): String? {
        return observeAuthInfo().firstOrNull()?.tokens?.access
    }

    suspend fun getRefreshToken(): String? {
        return observeAuthInfo().firstOrNull()?.tokens?.refresh
    }
}