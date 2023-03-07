package com.xinto.opencord.domain.message

import androidx.compose.runtime.Immutable
import com.github.materiiapps.partial.Partialize
import com.xinto.opencord.domain.user.DomainUser
import com.xinto.opencord.util.SimpleAstParser
import com.xinto.opencord.util.Timestamp
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Immutable
@Partialize // Is this even able to be partial?
data class DomainMessageMemberJoin(
    override val id: Long,
    override val channelId: Long,
    override val timestamp: Instant,
    override val pinned: Boolean,
    override val content: String,
    override val author: DomainUser
) : DomainMessage, KoinComponent {
    override val contentNodes by lazy {
        get<SimpleAstParser>().parse(content, null)
    }
    override val formattedTimestamp by lazy {
        Timestamp.getFormattedTimestamp(timestamp)
    }
    override val isDeletable get() = true
}
