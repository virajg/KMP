package com.shaaya.kmpdailypluse.core.di

import com.shaaya.kmpdailypluse.core.database.DatabaseDriverFactory
import com.shaaya.kmpdailypluse.db.DailyPulseDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val databaseModule: Module = module {
    single { DatabaseDriverFactory(androidContext()).createDriver() }
    single { DailyPulseDatabase(get()) }
}
