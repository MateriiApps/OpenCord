package com.xinto.opencord.gateway.event

import com.xinto.opencord.gateway.io.EventName
import com.xinto.opencord.rest.dto.ApiGuildMemberChunk
import com.xinto.opencord.rest.dto.ApiMessage
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
            EventName.MESSAGE_CREATE -> {
                MessageCreateEvent(
                    data = decoder.decodeSerializableValue(ApiMessage.serializer())
                )
            }
            EventName.GUILD_MEMBER_CHUNK -> {
                GuildMemberChunkEvent(
                    data = decoder.decodeSerializableValue(ApiGuildMemberChunk.serializer())
                )
            }
            else -> null
        }
    }
}