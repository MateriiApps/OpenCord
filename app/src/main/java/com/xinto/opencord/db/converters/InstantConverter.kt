package com.xinto.opencord.db.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverter {

    @TypeConverter
    fun millisToInstant(millis: Long?): Instant? {
        return millis?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun instantToMillis(instant: Instant?): Long? {
        return instant?.toEpochMilliseconds()
    }

}