package com.xinto.opencord.domain.manager

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.xinto.opencord.proto.serializer.PersistentDataSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first

interface PersistentDataManager {
    suspend fun getSelectedGuildId(): ULong
    suspend fun setSelectedGuildId(id: ULong)

    suspend fun getSelectedChannelId(guildId: ULong): ULong
    suspend fun setSelectedChannelId(guildId: ULong, channelId: ULong)
}

class PersistentDataManagerImpl(
    private val context: Context,
    private val persistentDataSerializer: PersistentDataSerializer
) : PersistentDataManager {

    private val datastore = DataStoreFactory.create(
        serializer = persistentDataSerializer,
        produceFile = { context.dataStoreFile("persistent.pb") },
        corruptionHandler = null,
        migrations = emptyList(),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    )

    override suspend fun getSelectedGuildId(): ULong =
        datastore.data.first().selectedGuildId.toULong()

    override suspend fun setSelectedGuildId(id: ULong) {
        datastore.updateData {
            it.toBuilder()
                .setSelectedGuildId(id.toLong())
                .build()
        }
    }

    override suspend fun getSelectedChannelId(guildId: ULong): ULong {
        return datastore.data.first()
            .selectedChannelsMap[guildId.toLong()]
            ?.toULong() ?: 0UL
    }

    override suspend fun setSelectedChannelId(guildId: ULong, channelId: ULong) {
        datastore.updateData {
            it.toBuilder()
                .putSelectedChannels(guildId.toLong(), channelId.toLong())
                .build()
        }
    }
}
