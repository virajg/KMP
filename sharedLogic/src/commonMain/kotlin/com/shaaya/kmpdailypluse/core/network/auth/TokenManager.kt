package com.shaaya.kmpdailypluse.core.network.auth

/*
// =========================================================================================
// CENTRALIZED PRODUCTION TOKEN MANAGER WITH REAL REFRESH API CALL & BACKGROUND THREADING
// =========================================================================================
// Handles storing, retrieving, and thread-safe API refreshing of OAuth2 tokens.
// =========================================================================================

// Uncomment imports when enabling:
// import io.ktor.client.HttpClient
// import io.ktor.client.call.body
// import io.ktor.client.request.post
// import io.ktor.client.request.setBody
// import io.ktor.http.ContentType
// import io.ktor.http.contentType
// import kotlinx.coroutines.Dispatchers
// import kotlinx.coroutines.IO
// import kotlinx.coroutines.sync.Mutex
// import kotlinx.coroutines.sync.withLock
// import kotlinx.coroutines.withContext
// import kotlinx.serialization.Serializable

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)

// @Serializable
// data class RefreshTokenRequest(val refreshToken: String)

// @Serializable
// data class RefreshTokenResponse(val accessToken: String, val refreshToken: String)

object TokenManager {

    // 1. Thread-safety lock: Mutex ensures only ONE refresh API call is executed
    //    even if multiple requests fail with 401 simultaneously.
    // private val refreshMutex = Mutex()

    // 2. Separate unauthenticated Ktor client for auth endpoints (prevents infinite 401 loops)
    // private val authHttpClient = HttpClient()

    private var currentTokens: AuthTokens? = AuthTokens(
        accessToken = "DUMMY_INITIAL_ACCESS_TOKEN",
        refreshToken = "DUMMY_INITIAL_REFRESH_TOKEN"
    )

    fun getTokens(): AuthTokens? = currentTokens

    fun saveTokens(newAccessToken: String, newRefreshToken: String) {
        currentTokens = AuthTokens(newAccessToken, newRefreshToken)
    }

    fun clearTokens() {
        currentTokens = null
    }

    /**
     * Executes the HTTP API call to refresh tokens on a BACKGROUND THREAD (`Dispatchers.IO`).
     * Uses a Mutex lock to ensure concurrent 401 requests don't trigger duplicate API calls.
     *
     * @param oldRefreshToken The EXPIRED refresh token from `oldTokens?.refreshToken`
     * @return `AuthTokens` containing the NEW accessToken and NEW refreshToken from API
     */
    suspend fun refreshAccessToken(oldRefreshToken: String): AuthTokens? {
        // Production implementation running on Background I/O Thread (Dispatchers.IO):
        // return withContext(Dispatchers.IO) {
        //     refreshMutex.withLock {
        //         // Check if another concurrent thread already refreshed the token:
        //         val latestTokens = getTokens()
        //         if (latestTokens != null && latestTokens.refreshToken != oldRefreshToken) {
        //             return@withLock latestTokens // Return newly refreshed token
        //         }
        //
        //         try {
        //             // 3. Make real HTTP POST call to backend Refresh Token API
        //             val response: RefreshTokenResponse = authHttpClient.post("https://api.example.com/v1/auth/refresh") {
        //                 contentType(ContentType.Application.Json)
        //                 setBody(RefreshTokenRequest(refreshToken = oldRefreshToken))
        //             }.body()
        //
        //             // 4. Save NEW tokens received from API to encrypted storage
        //             saveTokens(response.accessToken, response.refreshToken)
        //             AuthTokens(response.accessToken, response.refreshToken)
        //         } catch (e: Exception) {
        //             // Refresh token expired or revoked -> clear local storage
        //             clearTokens()
        //             null
        //         }
        //     }
        // }

        val dummyNewAccessToken = "DUMMY_REFRESHED_ACCESS_TOKEN"
        val dummyNewRefreshToken = "DUMMY_REFRESHED_REFRESH_TOKEN"
        saveTokens(dummyNewAccessToken, dummyNewRefreshToken)
        return AuthTokens(dummyNewAccessToken, dummyNewRefreshToken)
    }
}
*/
