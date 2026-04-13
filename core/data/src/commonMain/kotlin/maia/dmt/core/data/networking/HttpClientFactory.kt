package maia.dmt.core.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import maia.dmt.core.data.dto.AuthTokensSerializable
import maia.dmt.core.data.dto.RefreshTokenRequest
import maia.dmt.core.data.mapper.toDomain
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.dto.LoginSuccessfulRequest
import maia.dmt.core.domain.logger.DmtLogger

class HttpClientFactory(
    private val dmtLogger: DmtLogger,
    private val sessionStorage: SessionStorage
) {

    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                    }
                )
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        dmtLogger.debug(message)
                        println("KTOR: $message")
                    }
                }
                level = LogLevel.ALL
            }
            install(WebSockets) {
                pingIntervalMillis = 20_000L
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = sessionStorage.getAccessToken()
                        val refreshToken = sessionStorage.getRefreshToken()
                        if (accessToken != null && refreshToken != null) {
                            BearerTokens(accessToken, refreshToken)
                        } else {
                            null
                        }
                    }
                    refreshTokens {
                        val refreshToken = sessionStorage.getRefreshToken() ?: return@refreshTokens null
                        try {
                            val response = client.post<RefreshTokenRequest, AuthTokensSerializable>(
                                route = "auth/tokens/refresh/",
                                body = RefreshTokenRequest(refresh = refreshToken)
                            )
                            when (response) {
                                is maia.dmt.core.domain.util.Result.Success -> {
                                    val newTokens = response.data.toDomain()
                                    val currentAuth = sessionStorage.observeAuthInfo().firstOrNull()
                                    sessionStorage.set(
                                        currentAuth?.copy(tokens = newTokens)
                                            ?: LoginSuccessfulRequest(tokens = newTokens)
                                    )
                                    BearerTokens(newTokens.access, newTokens.refresh)
                                }
                                is maia.dmt.core.domain.util.Result.Failure -> {
                                    sessionStorage.set(null)
                                    null
                                }
                            }
                        } catch (e: Exception) {
                            sessionStorage.set(null)
                            null
                        }
                    }
                }
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}
