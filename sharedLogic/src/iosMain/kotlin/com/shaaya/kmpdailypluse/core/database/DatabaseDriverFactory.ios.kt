package com.shaaya.kmpdailypluse.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.shaaya.kmpdailypluse.db.DailyPulseDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = DailyPulseDatabase.Schema,
            name = "DailyPulse.db"
        )
    }
}
