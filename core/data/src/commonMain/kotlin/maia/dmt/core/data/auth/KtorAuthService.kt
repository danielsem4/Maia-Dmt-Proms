package maia.dmt.core.data.auth

import io.ktor.client.HttpClient
import maia.dmt.core.data.dto.AuthResponseSerializable
import maia.dmt.core.data.dto.LoginRequest
import maia.dmt.core.data.dto.RefreshTokenRequest
import maia.dmt.core.data.dto.SelectClinicRequest
import maia.dmt.core.data.dto.Verify2faRequest
import maia.dmt.core.data.dto.AuthTokensSerializable
import maia.dmt.core.data.mapper.toAuthResult
import maia.dmt.core.data.mapper.toDomain
import maia.dmt.core.data.networking.delete
import maia.dmt.core.data.networking.post
import maia.dmt.core.domain.auth.AuthService
import maia.dmt.core.domain.dto.AuthResult
import maia.dmt.core.domain.dto.AuthTokens
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.asEmptyResult
import maia.dmt.core.domain.util.map

class KtorAuthService(
    private val httpClient: HttpClient
) : AuthService {

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthResult, DataError.Remote> {
        return httpClient.post<LoginRequest, AuthResponseSerializable>(
            route = "api/v1/mobile/auth/sessions/",
            body = LoginRequest(
                email = email,
                password = password
            )
        ).map { it.toAuthResult() }
    }

    override suspend fun verify2fa(
        userId: String,
        code: String
    ): Result<AuthResult, DataError.Remote> {
        return httpClient.post<Verify2faRequest, AuthResponseSerializable>(
            route = "api/v1/mobile/auth/2fa/verify/",
            body = Verify2faRequest(
                code = code,
                user_id = userId
            )
        ).map { it.toAuthResult() }
    }

    override suspend fun selectClinic(
        userId: String,
        clinicId: String
    ): Result<AuthResult, DataError.Remote> {
        return httpClient.post<SelectClinicRequest, AuthResponseSerializable>(
            route = "api/v1/mobile/auth/sessions/select-clinic/",
            body = SelectClinicRequest(
                clinic_id = clinicId,
                user_id = userId
            )
        ).map { it.toAuthResult() }
    }

    override suspend fun refreshToken(
        refreshToken: String
    ): Result<AuthTokens, DataError.Remote> {
        return httpClient.post<RefreshTokenRequest, AuthTokensSerializable>(
            route = "api/v1/mobile/auth/tokens/refresh/",
            body = RefreshTokenRequest(refresh = refreshToken)
        ).map { it.toDomain() }
    }

    override suspend fun logout(): EmptyResult<DataError.Remote> {
        return httpClient.delete<Unit>(
            route = "api/v1/mobile/auth/sessions/"
        ).asEmptyResult()
    }
}
