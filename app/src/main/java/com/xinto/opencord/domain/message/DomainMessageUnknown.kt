package com.xinto.opencord.domain.message

import androidx.compose.runtime.Immutable
import com.github.materiiapps.partial.Partialize
import com.xinto.opencord.domain.user.DomainUser
import com.xinto.opencord.util.Timestamp
import com.xinto.simpleast.Node
import kotlinx.datetime.Instant

@Immutable
@Partialize // Is this even able to be partial?
data class DomainMessageUnknown(
    override val id: Long,
    override val channelId: Long,
    override val timestamp: Instant,
    override val pinned: Boolean,
    override val content: String,
    override val author: DomainUser
) : DomainMessage {
    override val contentNodes = emptyList<Node<Any?>>()
    override val formattedTimestamp by lazy {
        Timestamp.getFormattedTimestamp(timestamp)
    }
    override val isDeletable get() = true
}
