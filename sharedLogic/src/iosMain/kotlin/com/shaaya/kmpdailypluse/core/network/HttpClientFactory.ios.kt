package com.shaaya.kmpdailypluse.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import platform.Foundation.NSURLSessionAuthChallengePerformDefaultHandling
// Uncomment imports below when enabling iOS SSL Pinning:
// import platform.Foundation.NSURLAuthenticationMethodServerTrust
// import platform.Foundation.NSURLCredential
// import platform.Foundation.NSURLSessionAuthChallengeCancelAuthenticationChallenge
// import platform.Foundation.NSURLSessionAuthChallengeUseCredential
// import platform.Foundation.credentialForTrust
// import platform.Security.SecTrustEvaluateWithError

/*
// =========================================================================
// 1. iOS SSL CERTIFICATE / PUBLIC KEY PIN STORAGE
// =========================================================================
// Define your target hostname and trusted SHA-256 public key fingerprints:
private const val PINNED_HOST = "newsapi.org"
private val ALLOWED_SHA256_PINS = setOf(
    "sha256/47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=", // Primary Pin
    "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB="  // Backup Pin
)
// =========================================================================
*/

@OptIn(ExperimentalForeignApi::class)
actual fun createPlatformHttpClient(): HttpClient {
    val engine = Darwin.create {
        configureSession {
            // Additional NSURLSessionConfiguration customization if needed
        }

        handleChallenge { session, task, challenge, completionHandler ->
            /*
            // =========================================================================
            // 2. iOS SSL CERTIFICATE PINNING VERIFICATION (Darwin / NSURLSessionDelegate)
            // =========================================================================
            val protectionSpace = challenge.protectionSpace
            val host = protectionSpace.host

            // Check if request matches target pinned domain and server trust authentication
            if (protectionSpace.authenticationMethod == NSURLAuthenticationMethodServerTrust && host == PINNED_HOST) {
                val serverTrust = protectionSpace.serverTrust

                if (serverTrust != null) {
                    val isTrusted = SecTrustEvaluateWithError(serverTrust, null)

                    if (isTrusted) {
                        // Compare server certificate public key against ALLOWED_SHA256_PINS
                        val credential = NSURLCredential.credentialForTrust(serverTrust)
                        completionHandler(
                            NSURLSessionAuthChallengeUseCredential.convert(),
                            credential
                        )
                        return@handleChallenge
                    }
                }

                // Reject connection if SSL pinning or trust evaluation fails:
                completionHandler(
                    NSURLSessionAuthChallengeCancelAuthenticationChallenge.convert(),
                    null
                )
                return@handleChallenge
            }
            // =========================================================================
            */

            // Default handling (allows standard OS SSL certificate evaluation without pinning):
            completionHandler(NSURLSessionAuthChallengePerformDefaultHandling.convert(), null)
        }
    }
    return createCommonHttpClient(engine)
}
