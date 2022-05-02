package com.xinto.opencord.proto.serializer

import androidx.datastore.core.Serializer
import com.xinto.opencord.proto.PersistentDataProto
import com.xinto.opencord.util.Logger
import java.io.InputStream
import java.io.OutputStream

class PersistentDataSerializer(
    private val logger: Logger
) : Serializer<PersistentDataProto> {

    override val defaultValue: PersistentDataProto =
        PersistentDataProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PersistentDataProto {
        return try {
            PersistentDataProto.parseFrom(input)
        } catch (t: Throwable) {
            logger.error(
                "PersistentData",
                "Failed to parse datastore",
                t
            )
            PersistentDataProto.getDefaultInstance()
        }
    }

    override suspend fun writeTo(
        t: PersistentDataProto,
        output: OutputStream
    ) {
        t.writeTo(output)
    }
}