package com.shaaya.kmpdailypluse.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Uncomment imports below when enabling Ktor Auth / E2E Encryption plugins:
// import io.ktor.client.plugins.auth.Auth
// import io.ktor.client.plugins.auth.providers.BearerTokens
// import io.ktor.client.plugins.auth.providers.bearer
// import io.ktor.client.plugins.api.createClientPlugin

expect fun createPlatformHttpClient(): HttpClient

/*
// =========================================================================================
// KTOR 3.x CLIENT PLUGIN FOR CENTRAL E2E REQUEST ENCRYPTION & RESPONSE DECRYPTION
// =========================================================================================
// In Ktor 3.x, `createClientPlugin` is the recommended way to intercept outbound requests
// and inbound responses centrally for both Android & iOS:
//
// val E2EEncryptionPlugin = createClientPlugin("E2EEncryptionPlugin") {
//     onRequest { request, content ->
//         val rawJsonBody = (content as? TextContent)?.text
//         if (!rawJsonBody.isNullOrEmpty()) {
//             val encryptedPayload = E2ECryptoManager.encryptRequestPayload(rawJsonBody)
//             request.headers.append("X-Encrypted-AES-Key", encryptedPayload.encryptedAesKey)
//             request.headers.append("X-AES-IV", encryptedPayload.iv)
//             // Replace request body with encrypted payload string
//         }
//     }
//     onResponse { response ->
//         val encryptedData = response.bodyAsText()
//         val aesKey = response.headers["X-Encrypted-AES-Key"] ?: ""
//         val iv = response.headers["X-AES-IV"] ?: ""
//         val plainJson = E2ECryptoManager.decryptResponsePayload(encryptedData, aesKey, iv)
//         // Pass plain JSON byte channel to ContentNegotiation parser
//     }
// }
// =========================================================================================
*/

fun createCommonHttpClient(
    engine: HttpClientEngine,
    json: Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }
): HttpClient {
    return HttpClient(engine) {
        install(ContentNegotiation) {
            json(json)
        }

        install(Logging) {
            level = LogLevel.ALL
        }

        defaultRequest {
            header(HttpHeaders.UserAgent, "KMPDailyPulse/1.0")
        }

        /*
        // =========================================================================================
        // KTOR AUTH PLUGIN FOR HTTP 401 RETRY & AUTOMATIC TOKEN REFRESH (ANDROID & IOS)
        // =========================================================================================
        // 1. `oldTokens`: Automatically provided by Ktor. Contains the EXPIRED refreshToken that failed.
        // 2. `TokenManager.refreshAccessToken(...)`: Executes network call on background I/O thread.
        // 3. `newTokens`: The fresh `AuthTokens` received from the backend refresh API call.
        // 4. Returns `BearerTokens(...)` to Ktor -> Ktor automatically RETRIES the failed API call.
        //
        // install(Auth) {
        //     bearer {
        //         loadTokens {
        //             val tokens = TokenManager.getTokens()
        //             if (tokens != null) {
        //                 BearerTokens(tokens.accessToken, tokens.refreshToken)
        //             } else null
        //         }
        //
        //         refreshTokens {
        //             // `oldTokens` contains the EXPIRED tokens that caused the 401:
        //             val expiredRefreshToken = oldTokens?.refreshToken ?: return@refreshTokens null
        //
        //             // Call backend API (on Background Thread / Dispatchers.IO) to get NEW tokens:
        //             val newTokens = TokenManager.refreshAccessToken(expiredRefreshToken)
        //
        //             if (newTokens != null) {
        //                 // Pass new tokens to Ktor -> Ktor retries original API request automatically
        //                 BearerTokens(newTokens.accessToken, newTokens.refreshToken)
        //             } else {
        //                 // Refresh API failed/expired -> clear storage and trigger logout
        //                 TokenManager.clearTokens()
        //                 null
        //             }
        //         }
        //     }
        // }
        // =========================================================================================
        */

        // To install E2E Encryption plugin when ready:
        // install(E2EEncryptionPlugin)
    }
}
