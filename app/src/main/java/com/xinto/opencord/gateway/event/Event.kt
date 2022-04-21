package com.xinto.opencord.gateway.event

import com.xinto.opencord.gateway.dto.MessageDeleteData
import com.xinto.opencord.gateway.dto.Ready
import com.xinto.opencord.gateway.io.EventName
import com.xinto.opencord.rest.dto.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonElement

interface Event

class EventDeserializationStrategy(
    private val eventName: EventName
) : DeserializationStrategy<Event?> {

    override val descriptor: SerialDescriptor
        get() = JsonElement.serializer().descriptor

    override fun deserialize(decoder: Decoder): Event? {
        return when (eventName) {
            EventName.READY -> {
                ReadyEvent(
                    data = decoder.decodeSerializableValue(Ready.serializer())
                )
            }
            EventName.GUILD_MEMBER_CHUNK -> {
                GuildMemberChunkEvent(
                    data = decoder.decodeSerializableValue(ApiGuildMemberChunk.serializer())
                )
            }
            EventName.GUILD_CREATE -> {
                GuildCreateEvent(
                    data = decoder.decodeSerializableValue(ApiGuild.serializer())
                )
            }
            EventName.GUILD_UPDATE -> {
                GuildUpdateEvent(
                    data = decoder.decodeSerializableValue(ApiGuild.serializer())
                )
            }
            EventName.GUILD_DELETE -> {
                null //TODO
            }
            EventName.CHANNEL_CREATE -> {
                ChannelCreateEvent(
                    data = decoder.decodeSerializableValue(ApiChannel.serializer())
                )
            }
            EventName.CHANNEL_UPDATE -> {
                ChannelUpdateEvent(
                    data = decoder.decodeSerializableValue(ApiChannel.serializer())
                )
            }
            EventName.CHANNEL_DELETE -> {
                ChannelDeleteEvent(
                    data = decoder.decodeSerializableValue(ApiChannel.serializer())
                )
            }
            EventName.MESSAGE_CREATE -> {
                MessageCreateEvent(
                    data = decoder.decodeSerializableValue(ApiMessage.serializer())
                )
            }
            EventName.MESSAGE_UPDATE -> {
                MessageUpdateEvent(
                    data = decoder.decodeSerializableValue(ApiMessagePartial.serializer())
                )
            }
            EventName.MESSAGE_DELETE -> {
                MessageDeleteEvent(
                    data = decoder.decodeSerializableValue(MessageDeleteData.serializer())
                )
            }
            EventName.UNKNOWN -> null
        }
    }
}