package org.example.app.util

import android.text.format.DateUtils

/**
 * PUBLIC_INTERFACE
 * TimeUtils provides human-friendly time formatting helpers.
 */
object TimeUtils {
    // PUBLIC_INTERFACE
    /** Formats a timestamp to a relative time span like "5 min ago". */
    fun formatRelative(timeMillis: Long): String {
        return DateUtils.getRelativeTimeSpanString(
            timeMillis,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    }
}
