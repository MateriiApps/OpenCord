package com.xinto.opencord.store

import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.db.entity.reactions.toEntity
import com.xinto.opencord.domain.emoji.DomainEmoji
import com.xinto.opencord.domain.emoji.toDomain
import com.xinto.opencord.domain.message.reaction.DomainReaction
import com.xinto.opencord.domain.message.reaction.toDomain
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.event.MessageReactionAddEvent
import com.xinto.opencord.gateway.event.MessageReactionRemoveAllEvent
import com.xinto.opencord.gateway.event.MessageReactionRemoveEvent
import com.xinto.opencord.gateway.onEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter

/**
 * Represents a reaction count being modified (U) or all the reactions being removed from a message (D).
 * "Adding" a reaction cannot be possible since it would require a fetch to verify that no reactions already exist on a message that isn't cached.
 */
typealias ReactionEvent = Event<Nothing, ReactionUpdateData, ReactionRemoveAllData>

data class ReactionUpdateData(
    val emoji: DomainEmoji,
    val channelId: Long,
    val messageId: Long,
    val countDiff: Int,
    val meReacted: Boolean,
)

data class ReactionRemoveAllData(
    val messageId: Long,
    val channelId: Long,
)

interface ReactionStore {
    fun observeChannel(channelId: Long): Flow<ReactionEvent>

    /**
     * Returns reactions ordered by the index received and/or time from gateway
     */
    fun getReactions(messageId: Long): List<DomainReaction>
}

class ReactionStoreImpl(
    gateway: DiscordGateway,
    private val cache: CacheDatabase,
    private val currentUserStore: CurrentUserStore,
) : ReactionStore {
    private val _events = MutableSharedFlow<ReactionEvent>()

    override fun observeChannel(channelId: Long): Flow<ReactionEvent> {
        return _events.filter { event ->
            event.fold(
                onAdd = { false },
                onUpdate = { it.channelId == channelId },
                onDelete = { it.channelId == channelId },
            )
        }
    }

    override fun getReactions(messageId: Long): List<DomainReaction> {
        return cache.reactions().getReactions(messageId)
            .sortedBy { it.reactionCreated }
            .map { it.toDomain() }
    }

    init {
        gateway.onEvent<MessageCreateEvent> { event ->
            val reactions = event.data.reactions
                ?: return@onEvent
            val messageId = event.data.id.value
            val guildId = event.data.guildId?.value

            cache.reactions().insertReactions(reactions.map { it.toEntity(messageId, guildId) })
        }

        gateway.onEvent<MessageReactionAddEvent> { event ->
            val messageId = event.data.messageId.value
            val emojiId = event.data.emoji.id?.value
            val emojiName = event.data.emoji.name

            val currentUserId = currentUserStore.getCurrentUser()?.id
            val meReacted = event.data.userId.value == currentUserId

            _events.emit(
                ReactionEvent.Update(
                    ReactionUpdateData(
                        emoji = event.data.emoji.toDomain(),
                        messageId = messageId,
                        channelId = event.data.channelId.value,
                        countDiff = 1,
                        meReacted = meReacted,
                    ),
                ),
            )

            cache.reactions().updateReaction(messageId, emojiId, emojiName, meReacted, countDiff = 1)
        }

        gateway.onEvent<MessageReactionRemoveEvent> { event ->
            val messageId = event.data.messageId.value
            val emojiId = event.data.emoji.id?.value
            val emojiName = event.data.emoji.name

            val currentUserId = currentUserStore.getCurrentUser()?.id
            val meReacted = event.data.userId.value == currentUserId

            _events.emit(
                ReactionEvent.Update(
                    ReactionUpdateData(
                        emoji = event.data.emoji.toDomain(),
                        messageId = messageId,
                        channelId = event.data.channelId.value,
                        countDiff = -1,
                        meReacted = meReacted,
                    ),
                ),
            )

            cache.reactions().updateReaction(messageId, emojiId, emojiName, meReacted, countDiff = -1)
        }

        gateway.onEvent<MessageReactionRemoveAllEvent> { event ->
            val messageId = event.data.messageId.value

            _events.emit(
                ReactionEvent.Delete(
                    ReactionRemoveAllData(
                        messageId = messageId,
                        channelId = event.data.channelId.value,
                    ),
                ),
            )

            cache.reactions().deleteByMessage(messageId)
        }
    }
}
