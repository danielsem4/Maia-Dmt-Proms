package maia.dmt.core.data.auth

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.post
import maia.dmt.core.domain.auth.AuthService
import maia.dmt.core.data.dto.LoginRequest
import maia.dmt.core.data.dto.LoginSuccessfulRequestSerializable
import maia.dmt.core.data.mapper.toDomain
import maia.dmt.core.domain.dto.LoginSuccessfulRequest
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map

class KtorAuthService(
    private val httpClient: HttpClient
): AuthService {


    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginSuccessfulRequest, DataError.Remote> {

        return httpClient.post<LoginRequest, LoginSuccessfulRequestSerializable>(
            route = "login/",
            body = LoginRequest(
                email = email,
                password = password
            )
        ).map {
            it.toDomain()
        }

    }

}