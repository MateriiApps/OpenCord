package com.xinto.opencord.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xinto.opencord.db.Converters
import com.xinto.opencord.db.dao.MessagesDao
import com.xinto.opencord.db.entity.message.EntityAttachment
import com.xinto.opencord.db.entity.message.EntityMessage

@Database(
    version = 1,
    entities = [
        EntityMessage::class,
        EntityAttachment::class
    ],
)
@TypeConverters(Converters::class)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun messages(): MessagesDao
}
