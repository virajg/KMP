# Tasks: Robust Networking Layer Implementation

- `[x]` Dependency Management
    - `[x]` Update `libs.versions.toml` with `auth`, `logging`, and `okhttp`
    - `[x]` Update `sharedLogic/build.gradle.kts`
- `[x]` Core Networking Refactoring
    - `[x]` Expand `NetworkError.kt`
    - `[x]` Implement `SafeApiCall.kt`
    - `[x]` Create `HttpClientFactory` (Common/Android/iOS)
- `[x]` SSL Pinning & Auth Integration
    - `[x]` Add SSL Pinning in platform engines
    - `[x]` Add 401 Retry logic in `HttpClientFactory`
- `[x]` API Service Migration
    - `[x]` Update `ArticlesApiService.kt`
- `[x]` Verification
    - `[x]` Build and verify
