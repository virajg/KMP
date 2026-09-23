package com.shaaya.kmpdailypluse.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
// Uncomment imports below when enabling SSL Pinning:
// import okhttp3.CertificatePinner

actual fun createPlatformHttpClient(): HttpClient {
    val engine = OkHttp.create {
        config {
            retryOnConnectionFailure(true)

            /*
            // =========================================================================
            // Android SSL Certificate Pinning Example (OkHttp CertificatePinner)
            // =========================================================================
            // To enable SSL Certificate Pinning on Android:
            // 1. Obtain the SHA-256 fingerprint(s) of your server's SSL certificate or Public Key (SPKI).
            // 2. Replace the dummy domain and dummy SHA-256 pins below with your target host and valid pins.
            // 3. Uncomment this block and import okhttp3.CertificatePinner.

            val certificatePinner = CertificatePinner.Builder()
                .add("newsapi.org", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
                .add("*.newsapi.org", "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=")
                .build()

            certificatePinner(certificatePinner)
            // =========================================================================
            */
        }
    }
    return createCommonHttpClient(engine)
}
