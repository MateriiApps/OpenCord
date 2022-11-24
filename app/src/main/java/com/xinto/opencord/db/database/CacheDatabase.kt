package com.xinto.opencord.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xinto.opencord.db.Converters
import com.xinto.opencord.db.dao.*
import com.xinto.opencord.db.entity.channel.EntityChannel
import com.xinto.opencord.db.entity.guild.EntityGuild
import com.xinto.opencord.db.entity.message.EntityAttachment
import com.xinto.opencord.db.entity.message.EntityEmbed
import com.xinto.opencord.db.entity.message.EntityMessage
import com.xinto.opencord.db.entity.user.EntityUser

@Database(
    version = 1,
    entities = [
        EntityChannel::class,
        EntityGuild::class,
        EntityAttachment::class,
        EntityEmbed::class,
        EntityMessage::class,
        EntityUser::class,
    ],
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun channels(): ChannelsDao
    abstract fun guilds(): GuildsDao

    abstract fun messages(): MessagesDao
    abstract fun attachments(): AttachmentsDao
    abstract fun embeds(): EmbedsDao

    abstract fun users(): UsersDao
}
