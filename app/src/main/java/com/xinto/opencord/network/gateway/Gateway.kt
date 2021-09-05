package com.xinto.opencord.network.gateway

import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.network.gateway.data.incoming.Heartbeat
import com.xinto.opencord.network.gateway.data.outgoing.Identification
import com.xinto.opencord.network.gateway.data.outgoing.IdentificationClientState
import com.xinto.opencord.network.gateway.data.outgoing.IdentificationProperties
import com.xinto.opencord.network.gateway.io.IncomingPayload
import com.xinto.opencord.network.gateway.io.OutgoingPayload
import com.xinto.opencord.network.response.base.ApiResponse
import com.xinto.opencord.util.currentAccountToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.*
import kotlin.coroutines.CoroutineContext

typealias EventAction = (response: ApiResponse) -> Unit

class Gateway(
    private val gson: Gson,
) : WebSocketListener(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val listeners: MutableList<EventAction> = mutableListOf()

    private var heartbeatInterval: Long = 0
    private var sequenceNumber: Int = 0

    override fun onOpen(
        webSocket: WebSocket,
        response: Response
    ) {
        val payload = OutgoingPayload(
            op = 2,
            d = Identification(
                token = currentAccountToken,
                capabilities = 95,
                largeThreshold = 100,
                compress = false,
                properties = IdentificationProperties(
                    browser = "Discord Android",
                    browser_user_agent = "Discord-Android/${BuildConfig.DISCORD_VERSION_CODE}",
                    client_build_number = BuildConfig.DISCORD_VERSION_CODE,
                    client_version = "89.8 - Beta",
                    device = Build.MODEL + ", " + Build.PRODUCT,
                    os = "Android",
                    os_sdk_version = Build.VERSION.SDK_INT.toString(),
                    os_version = Build.VERSION.RELEASE,
                    system_locale = Locale.getDefault().toString().replace("_", "-")
                ),
                clientState = IdentificationClientState(
                    guildHashes = emptyMap(),
                    highestLastMessageId = 0,
                    readStateVersion = -1,
                    useruserGuildSettingsVersion = -1
                ),
            )
        )
        webSocket.send(gson.toJson(payload))
    }

    override fun onMessage(
        webSocket: WebSocket,
        text: String,
    ) {
        val (opCode, data, s, t) = gson.fromJson(text, IncomingPayload::class.java)

        sequenceNumber = s ?: 0

        when (opCode) {
            0 -> handleEvent(t!!, data)
            10 -> {
                launch {
                    heartbeatInterval = gson.fromJson(data, Heartbeat::class.java).heartbeat_interval
                    heartbeat(
                        webSocket = webSocket,
                        previousInterval = 0L
                    )
                }
            }
            11 -> {
                Log.d("gateway", "Heartbeat acknowledged")
            }
        }
    }

    private tailrec suspend fun heartbeat(
        webSocket: WebSocket,
        previousInterval: Long,
    ) {
        delay(
            if (previousInterval == 0L)
                (heartbeatInterval * Math.random()).toLong()
            else
                heartbeatInterval
        )
        webSocket.send(
            gson.toJson(
                OutgoingPayload(
                    op = 1,
                    d = sequenceNumber
                )
            )
        )
        heartbeat(webSocket, heartbeatInterval)
    }

    override fun onClosing(
        webSocket: WebSocket,
        code: Int,
        reason: String
    ) {
        Log.d("gateway", "Closing connection: $reason $code")
        webSocket.close(code, reason)
    }

    override fun onClosed(
        webSocket: WebSocket,
        code: Int,
        reason: String
    ) {
        Log.d("gateway", "Closed connection: $reason $code")
    }

    override fun onFailure(
        webSocket: WebSocket,
        t: Throwable,
        response: Response?
    ) {
        Log.d("gateway", "Gateway failure: $t")
    }

    private fun handleEvent(
        event: String,
        data: JsonElement?
    ) {
        notifyListeners(
            gson.fromJson(data,
                when (event) {
                    else -> Nothing::class.java
                }
            )
        )
    }

    fun onEvent(
        action: EventAction
    ) {
        listeners.add(action)
    }

    private fun notifyListeners(
        response: ApiResponse
    ) {
        listeners.forEach {
            it.invoke(response)
        }
    }

}