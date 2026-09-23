# Walkthrough - Robust Networking Layer Implementation

I have implemented a comprehensive and reusable networking layer using Ktor, following industry best practices for Kotlin Multiplatform.

## Key Features Implemented

### 1. Reusable HttpClient Factory
Created a centralized `HttpClient` factory (`HttpClientFactory.kt`) that handles:
- **Common Configuration**: JSON serialization, Logging, and common plugins.
- **Platform-Specific Engines**: Uses `OkHttp` for Android and `Darwin` for iOS, allowing deep platform integration.

### 2. Dynamic Error Handling
Implemented an "Interceptor-like" error mapping mechanism using `safeApiCall`:
- Automatically maps HTTP status codes (401, 403, 404, 5xx) to typed `NetworkError` objects.
- Handles connectivity issues (`IOException`) and parsing errors (`SerializationException`) gracefully.
- Provides a `toException()` helper for easy conversion to Kotlin's `Result` type.

### 3. 401 Retry Mechanism (Authenticator)
Integrated the Ktor `Auth` plugin with a `bearer` provider:
- **`loadTokens`**: Placeholder for retrieving saved tokens.
- **`refreshTokens`**: Placeholder for your token refresh logic. When a 401 Unauthorized error occurs, Ktor will automatically trigger this block and retry the request once if new tokens are provided.

### 4. SSL Pinning
Implemented platform-specific SSL pinning hooks:
- **Android**: Configured `CertificatePinner` within the `OkHttp` engine.
- **iOS**: Provided a `handleChallenge` block in the `Darwin` engine for custom certificate verification.

### 5. Dependency Injection (Koin)
Added a `NetworkModule.kt` to provide the `HttpClient` and `ArticlesApiService` throughout the app.

---

## File Changes Summary

### Core Layer
- [NEW] [HttpClientFactory.kt](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/commonMain/kotlin/com/shaaya/kmpdailypluse/core/network/HttpClientFactory.kt): Common `HttpClient` setup.
- [NEW] [HttpClientFactory.android.kt](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/androidMain/kotlin/com/shaaya/kmpdailypluse/core/network/HttpClientFactory.android.kt): Android OkHttp engine with SSL Pinning.
- [NEW] [HttpClientFactory.ios.kt](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/iosMain/kotlin/com/shaaya/kmpdailypluse/core/network/HttpClientFactory.ios.kt): iOS Darwin engine with challenge handling.
- [NEW] [SafeApiCall.kt](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/commonMain/kotlin/com/shaaya/kmpdailypluse/core/network/SafeApiCall.kt): Reusable utility for safe network requests and error mapping.
- [NEW] [NetworkModule.kt](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/commonMain/kotlin/com/shaaya/kmpdailypluse/core/di/NetworkModule.kt): Koin module for networking.
- [MODIFY] [NetworkError.kt](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/commonMain/kotlin/com/shaaya/kmpdailypluse/core/network/NetworkError.kt): Expanded with more specific error types.

### Data Layer
- [MODIFY] [ArticlesApiService.kt](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/commonMain/kotlin/com/shaaya/kmpdailypluse/data/remote/ArticlesApiService.kt): Simplified using `safeApiCall`.

---

## Verification Plan

### Automated Tests
- Successfully ran `:sharedLogic:assemble` to verify compilation for both Android and iOS targets.

> [!TIP]
> To use the new networking logic, initialize Koin in your `androidApp` or `sharedUI` modules and include the `networkModule`.
