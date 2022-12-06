package com.xinto.opencord.domain.model

import com.github.materiiapps.enumutil.FromValue
import kotlinx.datetime.Instant

@FromValue
enum class ActivityType(val value: Int) {
    Game(0),
    Streaming(1),
    Listening(2),
    Watching(3),
    Custom(4),
    Competing(5),
    Unknown(-1);

    companion object
}

sealed interface DomainActivity {
    val name: String
    val createdAt: Long
    val type: ActivityType
}

data class DomainActivityGame(
    override val name: String,
    override val createdAt: Long,
    val id: String?,
    val state: String,
    val details: String,
    val applicationId: Long,
    val party: DomainActivityParty?,
    val assets: DomainActivityAssets?,
    val secrets: DomainActivitySecrets?,
    val timestamps: DomainActivityTimestamp?,
) : DomainActivity {
    override val type = ActivityType.Game
}

data class DomainActivityStreaming(
    override val name: String,
    override val createdAt: Long,
    val id: String,
    val url: String,
    val state: String,
    val details: String,
    val assets: DomainActivityAssets,
) : DomainActivity {
    override val type = ActivityType.Streaming
}

data class DomainActivityListening(
    override val name: String,
    override val createdAt: Long,
    val id: String,
    val flags: Int,
    val state: String,
    val details: String,
    val syncId: String,
    val party: DomainActivityParty,
    val assets: DomainActivityAssets,
    val metadata: DomainActivityMetadata?,
    val timestamps: DomainActivityTimestamp,
) : DomainActivity {
    override val type = ActivityType.Listening
}

data class DomainActivityCustom(
    override val name: String,
    override val createdAt: Long,
    val status: String?,
    val emoji: DomainActivityEmoji?,
) : DomainActivity {
    override val type = ActivityType.Custom
}

// TODO: remove this once all activity types are implemented
data class DomainActivityUnknown(
    override val name: String,
    override val createdAt: Long,
) : DomainActivity {
    override val type = ActivityType.Unknown
}

// TODO: use a partial emoji instead
data class DomainActivityEmoji(
    val name: String?,
    val id: Long?,
    val animated: Boolean?,
)

data class DomainActivityTimestamp(
    val start: Instant?,
    val end: Instant?,
)

data class DomainActivityParty(
    val id: String?,
    val currentSize: Int?,
    val maxSize: Int?,
)

data class DomainActivityAssets(
    val largeImage: String?,
    val largeText: String?,
    val smallImage: String?,
    val smallText: String?,
)

data class DomainActivitySecrets(
    val join: String?,
    val spectate: String?,
    val match: String?,
)

//data class DomainActivityButton(
//    val label: String,
//    val url: String,
//)

data class DomainActivityMetadata(
    val albumId: String?,
    val artistIds: List<String>?,
    val contextUri: String?,
)
