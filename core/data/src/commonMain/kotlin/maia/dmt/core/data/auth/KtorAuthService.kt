package maia.dmt.core.data.auth

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.post
import maia.dmt.core.domain.auth.AuthService
import maia.dmt.core.data.dto.LoginRequest
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult

class KtorAuthService(
    private val httpClient: HttpClient
): AuthService {


    override suspend fun login(
        email: String,
        password: String
    ): EmptyResult<DataError.Remote> {

        return httpClient.post(
            route = "/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )

    }

}