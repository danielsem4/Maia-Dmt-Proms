package maia.dmt.core.data.sensors

import io.ktor.client.HttpClient
import maia.dmt.core.data.dto.sensors.SensorsDataServerRequest
import maia.dmt.core.data.networking.post
import maia.dmt.core.data.util.encodeValueToJson
import maia.dmt.core.domain.sensors.SensorsService
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result

class KtorSensorsService(
    private val httpClient: HttpClient,
) : SensorsService {

    override suspend fun uploadSensorsAggResults(request: Any): Result<Unit, DataError.Remote> {
        return when (request) {
            is SensorsDataServerRequest -> {
                val jsonBody = try {
                    encodeValueToJson(request)
                } catch (e: Exception) {
                    println("Error encoding to JSON: ${e.message}")
                    return Result.Failure(DataError.Remote.UNKNOWN)
                }

                httpClient.post(
                    route = "aggSensorData/",
                    body = jsonBody
                )
            }
            else -> {
                Result.Failure(DataError.Remote.UNKNOWN)
            }
        }
    }
}