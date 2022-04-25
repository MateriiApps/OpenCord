package com.xinto.opencord.domain.manager

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.xinto.opencord.proto.PersistentDataProto
import com.xinto.opencord.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.io.InputStream
import java.io.OutputStream

interface PersistentDataManager {
    suspend fun getSelectedGuildId(): ULong
    suspend fun setSelectedGuildId(id: ULong)

    suspend fun getSelectedChannelId(guildId: ULong): ULong
    suspend fun setSelectedChannelId(guildId: ULong, channelId: ULong)
}

class PersistentDataManagerImpl(val context: Context) : PersistentDataManager {
    private val datastore = DataStoreFactory.create(
        serializer = PersistentDataSerializer,
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

    private object PersistentDataSerializer : Serializer<PersistentDataProto>, KoinComponent {
        override val defaultValue: PersistentDataProto =
            PersistentDataProto.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): PersistentDataProto {
            return try {
                PersistentDataProto.parseFrom(input)
            } catch (t: Throwable) {
                get<Logger>().error(
                    "PersistentData",
                    "Failed to parse datastore",
                    t
                )

                PersistentDataProto.getDefaultInstance()
            }
        }

        override suspend fun writeTo(t: PersistentDataProto, output: OutputStream) =
            t.writeTo(output)
    }
}
