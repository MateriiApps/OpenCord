package com.xinto.opencord.db

import androidx.room.TypeConverter
import com.xinto.opencord.rest.dto.ApiEmbedField
import com.xinto.opencord.rest.dto.ApiMessageType
import com.xinto.opencord.rest.dto.fromValue
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("unused")
class Converters {
    @TypeConverter
    fun fromApiMessageType(type: ApiMessageType): Int {
        return type.value
    }

    @TypeConverter
    fun toApiMessageType(type: Int): ApiMessageType {
        return ApiMessageType.fromValue(type)
            ?: throw IllegalStateException("Unknown message type $type")
    }

    @TypeConverter
    fun fromEmbedFields(fields: List<ApiEmbedField>?): String? {
        return Json.encodeToString(fields ?: return null)
    }

    @TypeConverter
    fun toEmbedFields(fields: String?): List<ApiEmbedField>? {
        return Json.decodeFromString(fields ?: return null)
    }
}
