package com.shaaya.kmpdailypluse.core.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.shaaya.kmpdailypluse.db.DailyPulseDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = DailyPulseDatabase.Schema,
            context = context,
            name = "DailyPulse.db"
        )
    }
}
