package maia.dmt.core.data.networking

import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import kotlin.coroutines.coroutineContext
import java.net.SocketTimeoutException
import java.net.ConnectException
import java.net.UnknownHostException


actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
        val response = execute()
        handleResponse(response)
    } catch(e: UnknownHostException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch(e: UnresolvedAddressException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch(e: ConnectException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch(e: SocketTimeoutException) {
        Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: HttpRequestTimeoutException) {
        Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: SerializationException) {
        Result.Failure(DataError.Remote.SERIALIZATION_ERROR)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        Result.Failure(DataError.Remote.UNKNOWN)
    }
}