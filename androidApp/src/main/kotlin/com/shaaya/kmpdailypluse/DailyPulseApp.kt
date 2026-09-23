package com.shaaya.kmpdailypluse

import android.app.Application
import com.shaaya.kmpdailypluse.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class DailyPulseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@DailyPulseApp)
        }
    }
}
