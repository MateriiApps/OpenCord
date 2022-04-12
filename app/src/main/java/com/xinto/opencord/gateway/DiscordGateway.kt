package com.xinto.opencord.gateway

import android.os.Build
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.gateway.dto.*
import com.xinto.opencord.gateway.event.Event
import com.xinto.opencord.gateway.event.GuildMemberChunkEvent
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.io.EventName
import com.xinto.opencord.gateway.io.IncomingPayload
import com.xinto.opencord.gateway.io.OpCode
import com.xinto.opencord.gateway.io.OutgoingPayload
import com.xinto.opencord.util.Logger
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.util.*
import kotlin.coroutines.CoroutineContext

interface DiscordGateway : CoroutineScope {

    sealed interface State {
        object Started : State
        object Connected : State
        object Disconnected : State
        object Stopped : State
    }

    val events: SharedFlow<Event<*>>

    val state: SharedFlow<State>

    suspend fun connect()

    suspend fun disconnect()

    suspend fun requestGuildMembers(guildId: Long)

}

class DiscordGatewayImpl(
    private val client: HttpClient,
    private val json: Json,
    private val accountManager: AccountManager,
    private val logger: Logger
) : DiscordGateway {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Default

    private lateinit var webSocketSession: DefaultClientWebSocketSession

    private val _events = MutableSharedFlow<Event<*>>()
    override val events = _events.asSharedFlow()

    private val _state = MutableSharedFlow<DiscordGateway.State>()
    override val state = _state.asSharedFlow()

    private var sequenceNumber: Int = 0

    override suspend fun connect() {
        _state.emit(DiscordGateway.State.Started)
        try {
            webSocketSession = client.webSocketSession(BuildConfig.URL_GATEWAY)
            sendIdentification()
            _state.emit(DiscordGateway.State.Connected)
            listenToSocket()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun disconnect() {
        logger.debug("Gateway", "Disconnecting")
        webSocketSession.close()
        _state.emit(DiscordGateway.State.Disconnected)
    }

    private suspend fun listenToSocket() {
        webSocketSession.incoming.receiveAsFlow().buffer(Channel.UNLIMITED).map {
            val jsonString = when (it) {
                is Frame.Text -> it.readText()
                is Frame.Binary -> String(it.readBytes())
                else -> null
            }
            jsonString?.let { str -> json.decodeFromString<IncomingPayload>(str) }
        }.filterNotNull().collect { incomingPayload ->
            val (opCode, data, seqNum, eventName) = incomingPayload

            if (seqNum != null)
                sequenceNumber = seqNum

            when (opCode) {
                OpCode.DISPATCH -> {
                    data!!
                    when (eventName) {
                        EventName.MESSAGE_CREATE -> {
                            _events.emit(MessageCreateEvent(json.decodeFromJsonElement(data)))
                        }
                        EventName.GUILD_MEMBER_CHUNK -> {
                            _events.emit(GuildMemberChunkEvent(json.decodeFromJsonElement(data)))
                        }
                        else -> {}
                    }
                }
                OpCode.HEARTBEAT -> {}
                OpCode.RECONNECT -> {}
                OpCode.HELLO -> {
                    launch {
                        val interval = json.decodeFromJsonElement<Heartbeat>(data!!).heartbeatInterval
                        runHeartbeat(interval, initial = true)
                    }
                }
                OpCode.INVALID_SESSION -> {
                    logger.debug("Gateway", "invalid session")
                }
                OpCode.HEARTBEAT_ACK -> {
                    logger.debug("Gateway", "Heartbeat acked!")
                }
                else -> {}
            }
        }
    }

    private tailrec suspend fun runHeartbeat(
        interval: Long,
        initial: Boolean = false
    ) {
        val delay = if (initial) (interval * Math.random()).toLong() else interval
        delay(delay)
        sendHeartbeat()
        runHeartbeat(interval)
    }

    private suspend fun sendHeartbeat() {
        sendSerializedData(
            OutgoingPayload(
                opCode = OpCode.HEARTBEAT,
                data = sequenceNumber
            )
        )
    }

    private suspend fun sendIdentification() {
        sendSerializedData(
            OutgoingPayload(
                opCode = OpCode.IDENTIFY,
                data = Identification(
                    token = accountManager.currentAccountToken!!,
                    capabilities = 95,
                    largeThreshold = 100,
                    compress = false,
                    properties = IdentificationProperties(
                        browser = "Discord Android",
                        browserUserAgent = "Discord-Android/${BuildConfig.DISCORD_VERSION_CODE}",
                        clientBuildNumber = BuildConfig.DISCORD_VERSION_CODE,
                        clientVersion = "89.8 - Beta",
                        device = Build.MODEL + ", " + Build.PRODUCT,
                        os = "Android",
                        osSdkVersion = Build.VERSION.SDK_INT.toString(),
                        osVersion = Build.VERSION.RELEASE,
                        systemLocale = Locale.getDefault().toString().replace("_", "-")
                    ),
                    clientState = IdentificationClientState(
                        guildHashes = emptyMap(),
                        highestLastMessageId = 0,
                        readStateVersion = -1,
                        userGuildSettingsVersion = -1
                    ),
                )
            )
        )
    }

    override suspend fun requestGuildMembers(guildId: Long) {
        sendSerializedData(
            OutgoingPayload(
                opCode = OpCode.REQUEST_GUILD_MEMBERS,
                data = RequestGuildMembers(
                    guildId = guildId
                )
            )
        )
    }

    private suspend inline fun <reified T> sendSerializedData(data: T) {
        val json = json.encodeToString(data)
        webSocketSession.send(Frame.Text(json))
    }
}

inline fun <reified E : Event<*>> DiscordGateway.onEvent(
    noinline filterPredicate: suspend (E) -> Boolean = { true },
    crossinline block: suspend (E) -> Unit
) {
    events.buffer(Channel.UNLIMITED)
        .filterIsInstance<E>()
        .filter(filterPredicate)
        .onEach {
            launch {
                block(it)
            }
        }.launchIn(this)
}

inline fun DiscordGateway.scheduleOnConnection(
    crossinline block: suspend () -> Unit
) {
    state.buffer(Channel.UNLIMITED)
        .onEach {
            if (it is DiscordGateway.State.Connected) {
                block()
            }
        }.launchIn(this)
}