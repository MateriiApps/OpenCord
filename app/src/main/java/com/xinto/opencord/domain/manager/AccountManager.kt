package com.xinto.opencord.domain.manager

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.xinto.opencord.proto.serializer.AccountsSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first

interface AccountManager {
    suspend fun getCurrentToken(): String?
    suspend fun setCurrentToken(token: String?)

    suspend fun isLoggedIn(): Boolean =
        getCurrentToken()?.isNotEmpty() ?: false
}

class AccountManagerImpl(
    private val context: Context,
    private val accountsSerializer: AccountsSerializer,
) : AccountManager {

    private val datastore = DataStoreFactory.create(
        serializer = accountsSerializer,
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
}
