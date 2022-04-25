package com.xinto.opencord.domain.manager

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.xinto.opencord.proto.AccountsProto
import com.xinto.opencord.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.InputStream
import java.io.OutputStream

interface AccountManager {
    suspend fun getCurrentToken(): String?
    suspend fun setCurrentToken(token: String?)

    suspend fun isLoggedIn(): Boolean =
        getCurrentToken()?.isNotEmpty() ?: false
}

class AccountManagerImpl(val context: Context) : AccountManager {
    private val datastore = DataStoreFactory.create(
        serializer = AccountsProtoSerializer,
        produceFile = { context.dataStoreFile("accounts.pb") },
        corruptionHandler = null,
        migrations = emptyList(),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    )

    override suspend fun getCurrentToken(): String? =
        datastore.data.first().currentToken

    override suspend fun setCurrentToken(token: String?) {
        datastore.updateData {
            it.toBuilder()
                .setCurrentToken(token)
                .build()
        }
    }

    private object AccountsProtoSerializer : Serializer<AccountsProto>, KoinComponent {
        override val defaultValue: AccountsProto =
            AccountsProto.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): AccountsProto {
            return try {
                AccountsProto.parseFrom(input)
            } catch (t: Throwable) {
                val logger by inject<Logger>()
                logger.error("AccountsConfig", "Failed to parse account datastore!", t)

                AccountsProto.getDefaultInstance()
            }
        }

        override suspend fun writeTo(t: AccountsProto, output: OutputStream) =
            t.writeTo(output)
    }
}
