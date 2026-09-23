package com.shaaya.kmpdailypluse.core.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

val sharedModules = listOf(networkModule, articlesModule)

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(sharedModules)
    }
