package com.xinto.opencord.domain.mapper

import com.xinto.opencord.domain.model.DomainCustomStatus
import com.xinto.opencord.domain.model.DomainFriendSources
import com.xinto.opencord.domain.model.DomainGuildFolder
import com.xinto.opencord.domain.model.DomainUserSettingsPartial
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
        expiresAt = "", // TODO: fix this here
        emojiId = emojiId?.let { ApiSnowflake(it) },
        emojiName = emojiName,
    )
}
