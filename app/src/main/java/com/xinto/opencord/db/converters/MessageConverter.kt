package com.xinto.opencord.db.converters

import androidx.room.TypeConverter
import com.xinto.opencord.db.entity.EntityMessageType
import com.xinto.opencord.db.entity.fromValue

class MessageConverter {

    @TypeConverter
    fun messageTypeToValue(messageType: EntityMessageType?): Int? {
        return messageType?.value
    }

    @TypeConverter
    fun valueToMessageType(messageType: Int?): EntityMessageType? {
        return messageType?.let { EntityMessageType.fromValue(it) }
    }

}