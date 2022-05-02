package com.xinto.opencord.proto.serializer

import androidx.datastore.core.Serializer
import com.xinto.opencord.proto.AccountsProto
import com.xinto.opencord.util.Logger
import java.io.InputStream
import java.io.OutputStream

class AccountsSerializer(
    private val logger: Logger
) : Serializer<AccountsProto> {

    override val defaultValue: AccountsProto =
        AccountsProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AccountsProto {
        return try {
            AccountsProto.parseFrom(input)
        } catch (t: Throwable) {
            logger.error(
                "AccountsConfig",
                "Failed to parse datastore",
                t
            )

            AccountsProto.getDefaultInstance()
        }
    }

    override suspend fun writeTo(
        t: AccountsProto,
        output: OutputStream
    ) {
        t.writeTo(output)
    }
}