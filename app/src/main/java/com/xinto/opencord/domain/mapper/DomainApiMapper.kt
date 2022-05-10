package com.xinto.opencord.domain.mapper

import com.xinto.opencord.domain.model.DomainCustomStatus
import com.xinto.opencord.domain.model.DomainFriendSources
import com.xinto.opencord.domain.model.DomainGuildFolder
import com.xinto.opencord.domain.model.DomainUserSettingsPartial
import com.xinto.opencord.rest.dto.*

fun DomainUserSettingsPartial.toApi(): ApiUserSettingsPartial {
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
        theme = theme?.value,
        developerMode = developerMode,
        guildPositions = guildPositions?.map { ApiSnowflake(it) },
        detectPlatformAccounts = detectPlatformAccounts,
        status = status?.value,
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
        friendSourceFlags = friendSourceFlags?.toApi(),
        guildFolders = guildFolders?.map { it.toApi() },
        customStatus = customStatus?.toApi(),
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
