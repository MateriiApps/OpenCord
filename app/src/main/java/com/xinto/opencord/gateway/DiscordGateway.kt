package com.xinto.opencord.gateway

import android.os.Build
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.gateway.dto.*
import com.xinto.opencord.gateway.event.Event
import com.xinto.opencord.gateway.event.EventDeserializationStrategy
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.io.IncomingPayload
import com.xinto.opencord.gateway.io.OpCode
import com.xinto.opencord.gateway.io.OutgoingPayload
import com.xinto.opencord.rest.dto.ApiSnowflake
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

    val events: SharedFlow<Event>

    val state: SharedFlow<State>

    suspend fun connect()

    suspend fun disconnect()

    suspend fun requestGuildMembers(guildId: ULong)

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
    private var establishConnection = true

    private val _events = MutableSharedFlow<Event>()
    override val events = _events.asSharedFlow()

    private val _state = MutableSharedFlow<DiscordGateway.State>()
    override val state = _state.asSharedFlow()

    private var sequenceNumber: Int = 0
    private lateinit var sessionId: String

    override suspend fun connect() {
        _state.emit(DiscordGateway.State.Started)

        while (establishConnection) {
            try {
                establishConnection = false

                webSocketSession = client.webSocketSession(BuildConfig.URL_GATEWAY)
                sendIdentification()
                _state.emit(DiscordGateway.State.Connected)
                listenToSocket()

                val reason = withTimeoutOrNull(1500L) {
                    webSocketSession.closeReason.await()
                } ?: return
                val closeCode = CloseCode.fromCode(reason.code.toInt())
                establishConnection = closeCode.canReconnect
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        _state.emit(DiscordGateway.State.Stopped)
    }

    override suspend fun disconnect() {
        logger.debug("Gateway", "Disconnecting")
        webSocketSession.close()
        _state.emit(DiscordGateway.State.Disconnected)
    }

    override suspend fun requestGuildMembers(guildId: ULong) {
        sendSerializedData(
            OutgoingPayload(
                opCode = OpCode.REQUEST_GUILD_MEMBERS,
                data = RequestGuildMembers(
                    guildId = ApiSnowflake(guildId)
                )
            )
        )
    }

    private suspend fun listenToSocket() {
        webSocketSession.incoming.receiveAsFlow().buffer(Channel.UNLIMITED).map {
            val jsonString = when (it) {
                is Frame.Text -> it.readText()
                is Frame.Binary -> String(it.readBytes())
                else -> null
            }
            jsonString?.let { str ->
                logger.debug("Gateway", str)
                json.decodeFromString<IncomingPayload>(str)
            }
        }.filterNotNull().collect { incomingPayload ->
            val (opCode, data, seqNum, eventName) = incomingPayload

            if (seqNum != null)
                sequenceNumber = seqNum

            when (opCode) {
                OpCode.DISPATCH -> {
                    val decoded = json.decodeFromJsonElement(EventDeserializationStrategy(eventName!!), data!!)
                    decoded?.let {
                        if (it is ReadyEvent) {
                            sessionId = it.data.sessionId
                        }
                        _events.emit(it)
                    }
                }
                OpCode.HEARTBEAT -> {}
                OpCode.RECONNECT -> {}
                OpCode.HELLO -> {
                    launch {
                        val interval =
                            json.decodeFromJsonElement<Heartbeat>(data!!).heartbeatInterval
                        runHeartbeat(interval, initial = true)
                    }
                }
                OpCode.INVALID_SESSION -> {
                    val canResume = json.decodeFromJsonElement<Boolean>(data!!)
                    if (canResume) {
                        sendResume()
                    }
                    logger.debug("Gateway", "Invalid Session, canResume: $canResume")
                }
                OpCode.HEARTBEAT_ACK -> {
                    logger.debug("Gateway", "Heartbeat Acked!")
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
        sendPayload(
            opCode = OpCode.HEARTBEAT,
            data = sequenceNumber
        )
    }

    private suspend fun sendIdentification() {
        sendPayload(
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
    }

    private suspend fun sendResume() {
        sendPayload(
            opCode = OpCode.RESUME,
            data = Resume(
                token = accountManager.currentAccountToken!!,
                sessionId = sessionId,
                sequenceNumber = sequenceNumber
            )
        )
    }

    private suspend inline  fun <reified T> sendPayload(opCode: OpCode, data: T?) {
        sendSerializedData(
            OutgoingPayload(
                opCode = opCode,
                data = data
            )
        )
    }

    private suspend inline fun <reified T> sendSerializedData(data: T) {
        val json = json.encodeToString(data)
        webSocketSession.send(Frame.Text(json))
    }
}

inline fun <reified E : Event> DiscordGateway.onEvent(
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