package com.xinto.opencord.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xinto.opencord.db.converters.InstantConverter
import com.xinto.opencord.db.converters.MessageConverter
import com.xinto.opencord.db.dao.MessagesDao
import com.xinto.opencord.db.entity.EntityAttachment
import com.xinto.opencord.db.entity.EntityEmbed
import com.xinto.opencord.db.entity.EntityMessage
import com.xinto.opencord.db.entity.EntityUser

@Database(
    entities = [
        EntityMessage::class,
        EntityUser::class,
        EntityAttachment::class,
        EntityEmbed::class
    ],
    version = 1
)
@TypeConverters(MessageConverter::class, InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messagesDao(): MessagesDao

}