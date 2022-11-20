package com.xinto.opencord.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xinto.opencord.db.Converters
import com.xinto.opencord.db.dao.MessagesDao
import com.xinto.opencord.db.dao.UsersDao
import com.xinto.opencord.db.entity.message.EntityAttachment
import com.xinto.opencord.db.entity.message.EntityEmbed
import com.xinto.opencord.db.entity.message.EntityMessage
import com.xinto.opencord.db.entity.message.EntityUser

@Database(
    version = 1,
    entities = [
        EntityMessage::class,
        EntityAttachment::class,
        EntityEmbed::class,
        EntityUser::class,
    ],
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun messages(): MessagesDao
    abstract fun users(): UsersDao
}
