package com.xinto.opencord.domain.mapper

import androidx.compose.ui.graphics.Color
import com.github.materiiapps.partial.map
import com.xinto.opencord.domain.model.*
import com.xinto.opencord.rest.dto.*
import com.xinto.opencord.rest.service.DiscordCdnServiceImpl
import kotlinx.datetime.Instant

fun ApiAttachment.toDomain(): DomainAttachment {
    return if (contentType.isNotEmpty()) {
        when (contentType) {
            "video/mp4" -> DomainAttachment.Video(
                id = id.value,
                filename = filename,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100,
            )
            else -> DomainAttachment.Picture(
                id = id.value,
                filename = filename,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100,
            )
        }
    } else {
        DomainAttachment.File(
            id = id.value,
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
        )
    }
}

fun ApiChannel.toDomain(): DomainChannel {
    return when (type) {
        2 -> DomainChannel.VoiceChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
        )
        4 -> DomainChannel.Category(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
        )
        5 -> DomainChannel.AnnouncementChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            nsfw = nsfw,
        )
        else -> DomainChannel.TextChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            nsfw = nsfw,
        )
    }
}

fun ApiGuild.toDomain(): DomainGuild {
    return DomainGuild(
        id = id.value,
        name = name,
        iconUrl = icon?.let { icon ->
            DiscordCdnServiceImpl.getGuildIconUrl(id.toString(), icon)
        },
        bannerUrl = banner?.let { banner ->
            DiscordCdnServiceImpl.getGuildBannerUrl(id.toString(), banner)
        },
        premiumTier = premiumTier,
        premiumSubscriptionCount = premiumSubscriptionCount ?: 0,
    )
}

fun ApiGuildMember.toDomain(): DomainGuildMember {
    val avatarUrl = user?.let { user ->
        avatar
            ?.let { DiscordCdnServiceImpl.getUserAvatarUrl(user.id.toString(), it) }
            ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(user.discriminator.toInt().rem(5))
    }
    return DomainGuildMember(
        user = user?.toDomain(),
        nick = nick,
        avatarUrl = avatarUrl,
    )
}

fun ApiGuildMemberChunk.toDomain(): DomainGuildMemberChunk {
    val domainMembers = members.map { it.toDomain() }
    return DomainGuildMemberChunk(
        guildId = guildId.value,
        guildMembers = domainMembers,
        chunkIndex = chunkIndex,
        chunkCount = chunkCount,
    )
}

fun ApiMessage.toDomain(): DomainMessage {
    return when (type) {
        ApiMessageType.Default, ApiMessageType.Reply -> {
            DomainMessageRegular(
                id = id.value,
                channelId = channelId.value,
                content = content,
                author = author.toDomain(),
                timestamp = timestamp,
                pinned = pinned,
                editedTimestamp = editedTimestamp,
                attachments = attachments.map { it.toDomain() },
                embeds = embeds.map { it.toDomain() },
                isReply = type == ApiMessageType.Reply,
                referencedMessage = referencedMessage?.toDomain() as? DomainMessageRegular,
                mentionEveryone = mentionEveryone,
                mentions = mentions.map { it.toDomain() },
            )
        }
        ApiMessageType.GuildMemberJoin -> {
            DomainMessageMemberJoin(
                id = id.value,
                content = content,
                channelId = channelId.value,
                timestamp = timestamp,
                pinned = pinned,
                author = author.toDomain(),
            )
        }
        else -> DomainMessageUnknown(
            id = id.value,
            content = content,
            channelId = channelId.value,
            timestamp = timestamp,
            pinned = pinned,
            author = author.toDomain(),
        )
    }
}

fun ApiMessagePartial.toDomain(): DomainMessageRegularPartial {
    return DomainMessageRegularPartial(
        id = id.map { it.value },
        content = content,
        channelId = channelId.map { it.value },
        author = author.map { it.toDomain() },
        timestamp = timestamp,
        editedTimestamp = editedTimestamp,
        attachments = attachments.map { attachments ->
            attachments.map { it.toDomain() }
        },
        embeds = embeds.map { embeds ->
            embeds.map { it.toDomain() }
        },
    )
}

fun ApiUser.toDomain(): DomainUser {
    val avatarUrl = avatar
        ?.let { DiscordCdnServiceImpl.getUserAvatarUrl(id.toString(), it) }
        ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(discriminator.toInt().rem(5))

    return when {
        locale != null -> DomainUserPrivate(
            id = id.value,
            username = username,
            discriminator = discriminator,
            avatarUrl = avatarUrl,
            bot = bot,
            bio = bio,
            flags = (publicFlags ?: 0) or (privateFlags ?: 0),
            pronouns = pronouns,
            mfaEnabled = mfaEnabled!!,
            verified = verified!!,
            email = email!!,
            phone = phone,
            locale = locale,
        )
        premium != null -> DomainUserReadyEvent(
            id = id.value,
            username = username,
            discriminator = discriminator,
            avatarUrl = avatarUrl,
            bot = bot,
            bio = bio,
            flags = privateFlags ?: 0,
            mfaEnabled = mfaEnabled!!,
            verified = verified!!,
            premium = premium,
            purchasedFlags = purchasedFlags!!,
        )
        else -> DomainUserPublic(
            id = id.value,
            username = username,
            discriminator = discriminator,
            avatarUrl = avatarUrl,
            bot = bot,
            bio = bio,
            flags = (publicFlags ?: 0) or (privateFlags ?: 0),
            pronouns = pronouns,
        )
    }
}

fun ApiPermissions.toDomain(): List<DomainPermission> {
    return DomainPermission.values().filter {
        (value and it.flags) == it.flags
    }
}

fun ApiEmbed.toDomain(): DomainEmbed {
    val domainAuthor = author?.toDomain()
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.let {
            Color(red = it.red, green = it.green, blue = it.blue)
        },
        author = domainAuthor,
        fields = fields?.map { it.toDomain() },
    )
}

fun ApiEmbedAuthor.toDomain(): DomainEmbedAuthor {
    return DomainEmbedAuthor(
        name = name,
    )
}

fun ApiEmbedField.toDomain(): DomainEmbedField {
    return DomainEmbedField(
        name = name,
        value = value,
    )
}

fun ApiUserSettingsPartial.toDomain(): DomainUserSettingsPartial {
    return DomainUserSettingsPartial(
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
        theme = theme.map { DomainThemeSetting.fromValue(it)!! },
        developerMode = developerMode,
        detectPlatformAccounts = detectPlatformAccounts,
        status = status.map { DomainUserStatus.fromValue(it)!! },
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
        friendSourceFlags = friendSourceFlags.map { it.toDomain() },
        guildFolders = guildFolders.map { guildFolders ->
            guildFolders.map { it.toDomain() }
        },
        customStatus = customStatus.map { it?.toDomain() },
    )
}

fun ApiUserSettings.toDomain(): DomainUserSettings {
    val domainTheme = DomainThemeSetting.fromValue(theme)
        ?: throw IllegalArgumentException("Invalid theme $theme")
    val domainStatus = DomainUserStatus.fromValue(status)
        ?: throw IllegalArgumentException("Invalid status $status")

    return DomainUserSettings(
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
        theme = domainTheme,
        developerMode = developerMode,
        detectPlatformAccounts = detectPlatformAccounts,
        status = domainStatus,
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
        friendSourceFlags = friendSourceFlags.toDomain(),
        guildFolders = guildFolders.map { it.toDomain() },
        customStatus = customStatus?.toDomain(),
    )
}

fun ApiFriendSources.toDomain(): DomainFriendSources {
    return DomainFriendSources(
        all = (all ?: false) && mutualFriends == null && mutualGuilds == null,
        mutualFriends = mutualFriends ?: false,
        mutualGuilds = mutualGuilds ?: false,
    )
}

fun ApiGuildFolder.toDomain(): DomainGuildFolder {
    return DomainGuildFolder(
        id = id?.value,
        guildIds = guildIds.map { it.value },
        name = name,
    )
}

fun ApiCustomStatus.toDomain(): DomainCustomStatus {
    return DomainCustomStatus(
        text = text,
        expiresAt = expiresAt?.let { Instant.parse(it) },
        emojiId = emojiId?.value,
        emojiName = emojiName,
    )
}

fun ApiActivity.toDomain(): DomainActivity {
    return when (ActivityType.fromValue(this.type)) {
        ActivityType.Game -> DomainActivityGame(
            name = name,
            createdAt = createdAt ?: 0,
            id = id!!,
            state = state!!,
            details = details!!,
            applicationId = applicationId!!.value,
            party = party?.toDomain(),
            assets = assets?.toDomain(),
            secrets = secrets?.toDomain(),
            timestamps = timestamps?.toDomain(),
        )
        ActivityType.Streaming -> DomainActivityStreaming(
            name = name,
            createdAt = createdAt ?: 0,
            id = id!!,
            url = url!!,
            state = state!!,
            details = details!!,
            assets = assets!!.toDomain(),
        )
        ActivityType.Listening -> DomainActivityListening(
            name = name,
            createdAt = createdAt ?: 0,
            id = id!!,
            flags = flags!!,
            state = state!!,
            details = details!!,
            syncId = syncId!!,
            party = party!!.toDomain(),
            assets = assets!!.toDomain(),
            metadata = metadata?.toDomain(),
            timestamps = timestamps!!.toDomain(),
        )
        ActivityType.Custom -> DomainActivityCustom(
            name = name,
            createdAt = createdAt ?: 0,
            status = state,
            emoji = emoji?.toDomain(),
        )
        else -> DomainActivityUnknown(
            name = name,
            createdAt = createdAt ?: 0,
        )
    }
}

fun ApiActivityEmoji.toDomain(): DomainActivityEmoji {
    return DomainActivityEmoji(
        name = name,
        id = id?.value,
        animated = animated,
    )
}

fun ApiActivityTimestamp.toDomain(): DomainActivityTimestamp {
    return DomainActivityTimestamp(
        start = start?.let { Instant.fromEpochMilliseconds(it.toLong()) },
        end = end?.let { Instant.fromEpochMilliseconds(it.toLong()) },
    )
}

fun ApiActivityParty.toDomain(): DomainActivityParty {
    return DomainActivityParty(
        id = id,
        currentSize = size?.get(0),
        maxSize = size?.get(1),
    )
}

fun ApiActivityAssets.toDomain(): DomainActivityAssets {
    return DomainActivityAssets(
        largeImage = largeImage,
        largeText = largeText,
        smallImage = smallImage,
        smallText = smallText,
    )
}

fun ApiActivitySecrets.toDomain(): DomainActivitySecrets {
    return DomainActivitySecrets(
        join = join,
        spectate = spectate,
        match = match,
    )
}

//fun ApiActivityButton.toDomain(): DomainActivityButton {
//    return DomainActivityButton(
//        label = label,
//        url = url,
//    )
//}

fun ApiActivityMetadata.toDomain(): DomainActivityMetadata {
    return DomainActivityMetadata(
        albumId = albumId,
        artistIds = artistIds,
        contextUri = contextUri,
    )
}
