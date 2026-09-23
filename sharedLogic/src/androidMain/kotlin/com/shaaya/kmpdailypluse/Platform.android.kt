package com.shaaya.kmpdailypluse

import android.content.res.Resources
import android.os.Build
import kotlin.math.round

actual class Platform {
    actual val osName: String = "Android"
    actual val osVersion: String = "${Build.VERSION.SDK_INT}"
    actual val deviceModel: String = "${Build.MANUFACTURER} ${Build.MODEL}"
    actual val density: Int = round(Resources.getSystem().displayMetrics.density).toInt()
    actual fun logSystemInfo() = "($osName, $osVersion, $deviceModel, $density)"
}