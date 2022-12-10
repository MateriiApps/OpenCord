package com.xinto.opencord.db

import androidx.room.TypeConverter
import com.xinto.opencord.rest.models.embed.ApiEmbedField
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("unused")
class Converters {
    @TypeConverter
    fun fromEmbedFields(fields: List<ApiEmbedField>?): String? {
        return Json.encodeToString(fields ?: return null)
    }

    @TypeConverter
    fun toEmbedFields(fields: String?): List<ApiEmbedField>? {
        return Json.decodeFromString(fields ?: return null)
    }
}
