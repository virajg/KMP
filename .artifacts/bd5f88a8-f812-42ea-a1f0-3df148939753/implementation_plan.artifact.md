# Implementation Plan - Robust Networking Layer

Implement a reusable, secure, and robust networking layer using Ktor with dynamic error handling, 401 retry mechanism, and SSL pinning support.

## User Review Required

> [!IMPORTANT]
> I will be adding `ktor-client-auth`, `ktor-client-logging`, and `ktor-client-okhttp` dependencies.
> I will also implement `expect`/`actual` for `HttpClient` to support platform-specific configurations like SSL pinning.

## Proposed Changes

### 1. Dependency Updates
Update `libs.versions.toml` and `sharedLogic/build.gradle.kts` to include:
- `ktor-client-auth`
- `ktor-client-logging`
- `ktor-client-okhttp` (for Android)

### 2. Core Networking (KMP)
Create a centralized `HttpClient` factory and error handling logic.

#### [NEW] [HttpClientFactory.kt](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/commonMain/kotlin/com/shaaya/kmpdailypluse/core/network/HttpClientFactory.kt)
- Define `expect fun createHttpClient(engine: HttpClientEngine): HttpClient` or similar.
- Configure `ContentNegotiation` (JSON).
- Configure `Logging` for debugging.
- Configure `Auth` for 401 retry logic.
- Configure `HttpResponseValidator` for dynamic error mapping (Interceptor-like).

#### [NEW] [Platform-specific engines](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/androidMain/kotlin/com/shaaya/kmpdailypluse/core/network/HttpClientFactory.android.kt)
- Implement `actual` engine configuration.
- Add SSL Pinning using `CertificatePinner` for Android (OkHttp).
- Add SSL Pinning using `NSURLSessionDelegate` for iOS (Darwin).

### 3. Dynamic Error Handling
Refactor `NetworkError` and create a mapping mechanism.

#### [MODIFY] [NetworkError.kt](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/commonMain/kotlin/com/shaaya/kmpdailypluse/core/network/NetworkError.kt)
- Expand to include more specific errors (401, 403, 404, 500, etc.).

#### [NEW] [SafeApiCall.kt](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/commonMain/kotlin/com/shaaya/kmpdailypluse/core/network/SafeApiCall.kt)
- Create a utility function to execute network requests and wrap them in `NetworkResult` consistently.

### 4. Data Layer Refactoring
Update `ArticlesApiService` to use the new architecture.

#### [MODIFY] [ArticlesApiService.kt](file:///Users/neo/AndroidStudioProjects/KMPDailyPluse/sharedLogic/src/commonMain/kotlin/com/shaaya/kmpdailypluse/data/remote/ArticlesApiService.kt)
- Remove manual `try-catch`.
- Use `SafeApiCall`.
- Remove hardcoded API keys where possible (move to a better place if needed).

## Verification Plan

### Automated Tests
- Run `:sharedLogic:assemble` to verify compilation.
- (Optional) Add a unit test for `SafeApiCall` and `HttpResponseValidator`.

### Manual Verification
- Verify that 401 errors trigger the retry/refresh logic (if tokens are provided).
- Verify that network errors are correctly mapped to `NetworkError` types.
