package maia.dmt.core.domain.auth

import maia.dmt.core.domain.dto.AuthResult
import maia.dmt.core.domain.dto.AuthTokens
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.Result

interface AuthService {

    suspend fun login(
        email: String,
        password: String
    ): Result<AuthResult, DataError.Remote>

    suspend fun verify2fa(
        userId: String,
        code: String
    ): Result<AuthResult, DataError.Remote>

    suspend fun selectClinic(
        userId: String,
        clinicId: String
    ): Result<AuthResult, DataError.Remote>

    suspend fun refreshToken(
        refreshToken: String
    ): Result<AuthTokens, DataError.Remote>

    suspend fun logout(): EmptyResult<DataError.Remote>

}