package com.xinto.opencord.util

import android.content.Context
import android.text.format.DateFormat
import com.xinto.opencord.R
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*

object Timestamp : KoinComponent {
    private val context: Context by inject()

    private val timeOnlyFormat: SimpleDateFormat
        get() {
            val pattern = if (DateFormat.is24HourFormat(context)) "HH:mm" else "hh:mmaa"
            return SimpleDateFormat(pattern, Locale.getDefault())
        }

    private val dateOnlyFormat
        get() = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    fun getFormattedTimestamp(target: Instant): String {
        val systemTimeZone = TimeZone.currentSystemDefault()
        val currentDateTime = Clock.System.now().toLocalDateTime(systemTimeZone)
        val targetDateTime = target.toLocalDateTime(systemTimeZone)
        val date = Date(target.toEpochMilliseconds())

        val dayDifference = currentDateTime.dayOfYear - targetDateTime.dayOfYear
        val isSameYear = currentDateTime.year == targetDateTime.year
        val isTomorrow = isSameYear && dayDifference == -1
        val isToday = isSameYear && dayDifference == 0
        val isYesterday = isSameYear && dayDifference == 1

        if (isTomorrow)
            return context.getString(R.string.timestamp_tomorrow, timeOnlyFormat.format(date))

        if (isToday)
            return context.getString(R.string.timestamp_today, timeOnlyFormat.format(date))

        if (isYesterday)
            return context.getString(R.string.timestamp_yesterday, timeOnlyFormat.format(date))

        return dateOnlyFormat.format(date)
    }
}
