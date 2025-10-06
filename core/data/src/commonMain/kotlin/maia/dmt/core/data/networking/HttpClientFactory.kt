package maia.dmt.core.data.networking

import com.plcoding.core.data.BuildKonfig
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
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.logger.DmtLogger

/**
 *
 */

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
                    }
                }
                level = LogLevel.ALL
            }
            install(WebSockets) {
                pingIntervalMillis = 20_000L
            }
            defaultRequest {
                header("x-api-key", BuildKonfig.API_KEY)
                contentType(ContentType.Application.Json)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        sessionStorage
                            .observeAuthInfo()
                            .firstOrNull()
                            ?.let { authInfo ->
                                BearerTokens(
                                    accessToken = authInfo.token.toString(),
                                    refreshToken = ""
                                )
                            }
                    }

                    // Handle token refresh needs to be done, in feauture
                    refreshTokens {
                        sessionStorage
                            .observeAuthInfo()
                            .firstOrNull()
                            ?.let { authInfo ->
                                BearerTokens(
                                    accessToken = authInfo.token.toString(),
                                    refreshToken = ""
                                )
                            }
                    }
                }
            }
        }
    }
}