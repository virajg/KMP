package com.shaaya.kmpdailypluse.core.di

import com.shaaya.kmpdailypluse.core.network.createPlatformHttpClient
import com.shaaya.kmpdailypluse.data.remote.ArticlesApiService
import org.koin.dsl.module

val networkModule = module {
    single { createPlatformHttpClient() }
    single { ArticlesApiService(get()) }
}
