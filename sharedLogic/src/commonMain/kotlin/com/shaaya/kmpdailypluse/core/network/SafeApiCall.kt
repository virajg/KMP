package com.shaaya.kmpdailypluse.core.network

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

suspend fun <T> safeApiCall(block: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: RedirectResponseException) {
        NetworkResult.Error(mapToNetworkError(e.response.status.value))
    } catch (e: ClientRequestException) {
        NetworkResult.Error(mapToNetworkError(e.response.status.value))
    } catch (e: ServerResponseException) {
        NetworkResult.Error(mapToNetworkError(e.response.status.value))
    } catch (e: IOException) {
        val detail = e.message ?: e::class.simpleName ?: "IO Error"
        NetworkResult.Error(NetworkError.NoInternet("No Internet Connection ($detail)"))
    } catch (e: SerializationException) {
        val detail = e.message ?: "Failed to Parse Response"
        NetworkResult.Error(NetworkError.Serialization("Failed to Parse Response ($detail)"))
    } catch (e: Exception) {
        NetworkResult.Error(NetworkError.Unknown(e.message ?: "Unknown error"))
    }
}

private fun mapToNetworkError(statusCode: Int): NetworkError {
    return when (statusCode) {
        401 -> NetworkError.Unauthorized
        403 -> NetworkError.Forbidden
        404 -> NetworkError.NotFound
        in 500..599 -> NetworkError.ServerError
        else -> NetworkError.Unknown("Error Code: $statusCode")
    }
}
