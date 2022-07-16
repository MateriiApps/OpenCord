package com.xinto.opencord.domain.mapper

import com.xinto.opencord.domain.model.*
import com.xinto.opencord.rest.dto.*
import com.xinto.partialgen.mapToPartial

fun DomainUserSettingsPartial.toApi(): ApiUserSettingsPartial {
    val apiPartialTheme = theme.mapToPartial { it.value }
    val apiPartialGuildPositions = guildPositions.mapToPartial { guildPositions ->
        guildPositions.map { ApiSnowflake(it) }
    }
    val apiPartialStatus = status.mapToPartial { it.value }
    val apiPartialFriendSourceFlags = friendSourceFlags.mapToPartial { it.toApi() }
    val apiPartialGuildFolders = guildFolders.mapToPartial { guildFolders ->
        guildFolders.map { it.toApi() }
    }
    val apiPartialCustomStatus = customStatus.mapToPartial { it?.toApi() }
    return ApiUserSettingsPartial(
        locale = locale,
        showCurrentGame = showCurrentGame,
        inlineAttachmentMedia = inlineAttachmentMedia,
        inlineEmbedMedia = inlineEmbedMedia,
        gifAutoPlay = gifAutoPlay,
        renderEmbeds = renderEmbeds,
        renderReactions = renderReactions,
        animateEmoji = animateEmoji,
        enableTTSCommand = enableTTSCommand,
        messageDisplayCompact = messageDisplayCompact,
        convertEmoticons = convertEmoticons,
        disableGamesTab = disableGamesTab,
        theme = apiPartialTheme,
        developerMode = developerMode,
        guildPositions = apiPartialGuildPositions,
        detectPlatformAccounts = detectPlatformAccounts,
        status = apiPartialStatus,
        afkTimeout = afkTimeout,
        timezoneOffset = timezoneOffset,
        streamNotificationsEnabled = streamNotificationsEnabled,
        allowAccessibilityDetection = allowAccessibilityDetection,
        contactSyncEnabled = contactSyncEnabled,
        nativePhoneIntegrationEnabled = nativePhoneIntegrationEnabled,
        animateStickers = animateStickers,
        friendDiscoveryFlags = friendDiscoveryFlags,
        viewNsfwGuilds = viewNsfwGuilds,
        passwordless = passwordless,
        friendSourceFlags = apiPartialFriendSourceFlags,
        guildFolders = apiPartialGuildFolders,
        customStatus = apiPartialCustomStatus,
    )
}

fun DomainFriendSources.toApi(): ApiFriendSources {
    return ApiFriendSources(
        all = all,
        mutualFriends = mutualFriends,
        mutualGuilds = mutualGuilds,
    )
}

fun DomainGuildFolder.toApi(): ApiGuildFolder {
    return ApiGuildFolder(
        id = id?.let { ApiSnowflake(it) },
        guildIds = guildIds.map { ApiSnowflake(it) },
        name = name,
    )
}

fun DomainCustomStatus.toApi(): ApiCustomStatus {
    return ApiCustomStatus(
        text = text,
        expiresAt = null, // TODO: make timestamp serializer
        emojiId = emojiId?.let { ApiSnowflake(it) },
        emojiName = emojiName,
    )
}

fun DomainActivity.toApi(): ApiActivity {
    return when (this) {
        is DomainActivityGame -> ApiActivity(
            type = type.value,
            name = name,
            createdAt = createdAt,
            id = id,
            state = state,
            details = details,
            applicationId = ApiSnowflake(applicationId),
            party = party?.toApi(),
            assets = assets?.toApi(),
            secrets = secrets?.toApi(),
            timestamps = timestamps?.toApi(),
        )
        is DomainActivityStreaming -> ApiActivity(
            type = type.value,
            name = name,
            createdAt = createdAt,
            id = id,
            url = url,
            state = state,
            details = details,
            assets = assets.toApi()
        )
        is DomainActivityListening -> ApiActivity(
            type = type.value,
            name = name,
            createdAt = createdAt,
            id = id,
            flags = flags,
            state = state,
            details = details,
            syncId = syncId,
            party = party.toApi(),
            assets = assets.toApi(),
            metadata = metadata?.toApi(),
            timestamps = timestamps.toApi(),
        )
        is DomainActivityCustom -> ApiActivity(
            type = type.value,
            name = name,
            createdAt = createdAt,
            state = status,
            emoji = emoji?.toApi()
        )
        else -> {
            throw IllegalArgumentException("Cannot convert an unknown activity type to an api model!")
        }
    }
}

fun DomainActivityEmoji.toApi(): ApiActivityEmoji {
    return ApiActivityEmoji(
        name = name,
        id = id?.let { ApiSnowflake(it) },
        animated = animated,
    )
}

fun DomainActivityTimestamp.toApi(): ApiActivityTimestamp {
    return ApiActivityTimestamp(
        start = start?.toEpochMilliseconds().toString(),
        end = end?.toEpochMilliseconds().toString(),
    )
}

fun DomainActivityParty.toApi(): ApiActivityParty {
    return ApiActivityParty(
        id = id,
        size = if (currentSize == null || maxSize == null) null else {
            listOf(currentSize, maxSize)
        },
    )
}

fun DomainActivityAssets.toApi(): ApiActivityAssets {
    return ApiActivityAssets(
        largeImage = largeImage,
        largeText = largeText,
        smallImage = smallImage,
        smallText = smallText,
    )
}

fun DomainActivitySecrets.toApi(): ApiActivitySecrets {
    return ApiActivitySecrets(
        join = join,
        spectate = spectate,
        match = match,
    )
}

fun DomainActivityMetadata.toApi(): ApiActivityMetadata {
    return ApiActivityMetadata(
        albumId = albumId,
        artistIds = artistIds,
        contextUri = contextUri,
    )
}
