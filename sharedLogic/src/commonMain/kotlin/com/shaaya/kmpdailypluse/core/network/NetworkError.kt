package com.shaaya.kmpdailypluse.core.network

sealed class NetworkError(val message: String) {
    data class NoInternet(val details: String = "No Internet Connection") : NetworkError(details)
    data object Timeout : NetworkError("Request Timed Out")
    data object Unauthorized : NetworkError("Unauthorized Access (401)")
    data object Forbidden : NetworkError("Access Forbidden (403)")
    data object NotFound : NetworkError("Resource Not Found (404)")
    data object ServerError : NetworkError("Internal Server Error (500)")
    data class Serialization(val details: String = "Failed to Parse Response") : NetworkError(details)
    data class Unknown(val error: String = "Unknown Network Error") : NetworkError(error)

    fun toException(): Exception {
        return Exception(message)
    }
}
