package com.shaaya.kmpdailypluse.core.util

import kotlinx.datetime.Instant

expect fun currentTimeMillis(): Long

/**
 * Formats an ISO-8601 date string (e.g. "2024-09-22T10:15:30Z") into relative time
 * like "Just now", "5 minutes ago", "3 hours ago", "Yesterday", or "X days ago".
 */
fun formatRelativeDate(isoDateString: String): String {
    if (isoDateString.isBlank()) return isoDateString

    return try {
        val publishedTimeMillis = parseIsoToEpochMillis(isoDateString)
        val nowMillis = currentTimeMillis()

        val diffMillis = nowMillis - publishedTimeMillis
        if (diffMillis < 0) return "Just now"

        val seconds = diffMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        when {
            seconds < 60 -> "Just now"
            minutes < 60 -> "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
            hours < 24 -> "$hours ${if (hours == 1L) "hour" else "hours"} ago"
            days == 1L -> "Yesterday"
            days <= 30 -> "$days days ago"
            days <= 365 -> "${days / 30} ${if (days / 30 == 1L) "month" else "months"} ago"
            else -> "${days / 365} ${if (days / 365 == 1L) "year" else "years"} ago"
        }
    } catch (e: Exception) {
        isoDateString
    }
}

private fun parseIsoToEpochMillis(isoString: String): Long {
    return try {
        Instant.parse(isoString).toEpochMilliseconds()
    } catch (e: Exception) {
        0L
    }
}
