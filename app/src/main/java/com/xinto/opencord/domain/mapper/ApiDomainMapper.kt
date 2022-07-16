package com.xinto.opencord.domain.mapper

import androidx.compose.ui.graphics.Color
import com.xinto.opencord.domain.model.*
import com.xinto.opencord.rest.dto.*
import com.xinto.opencord.rest.service.DiscordCdnServiceImpl
import com.xinto.partialgen.mapToPartial
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
                height = height ?: 100
            )
            else -> DomainAttachment.Picture(
                id = id.value,
                filename = filename,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100
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
    val permissions = permissions.toDomain()
    return when (type) {
        2 -> DomainChannel.VoiceChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            permissions = permissions
        )
        4 -> DomainChannel.Category(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            permissions = permissions
        )
        5 -> DomainChannel.AnnouncementChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            permissions = permissions,
            nsfw = nsfw
        )
        else -> DomainChannel.TextChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            permissions = permissions,
            nsfw = nsfw
        )
    }
}

fun ApiGuild.toDomain(): DomainGuild {
    val iconUrl = icon?.let { icon ->
        DiscordCdnServiceImpl.getGuildIconUrl(id.toString(), icon)
    }
    val bannerUrl = banner?.let { banner ->
        DiscordCdnServiceImpl.getGuildBannerUrl(id.toString(), banner)
    }
    return DomainGuild(
        id = id.value,
        name = name,
        iconUrl = iconUrl,
        bannerUrl = bannerUrl,
        permissions = permissions.toDomain(),
        premiumTier = premiumTier,
        premiumSubscriptionCount = premiumSubscriptionCount ?: 0,
    )
}

fun ApiGuildMember.toDomain(): DomainGuildMember {
    val avatarUrl = user?.let { user ->
        avatar?.let { avatar ->
            DiscordCdnServiceImpl.getUserAvatarUrl(user.id.toString(), avatar)
        } ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(user.discriminator.toInt().rem(5))
    }
    val domainUser = user?.toDomain()
    return DomainGuildMember(
        user = domainUser,
        nick = nick,
        avatarUrl = avatarUrl
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

fun ApiMeGuild.toDomain(): DomainMeGuild {
    val iconUrl = icon?.let { icon ->
        DiscordCdnServiceImpl.getGuildIconUrl(id.toString(), icon)
    }
    return DomainMeGuild(
        id = id.value,
        name = name,
        iconUrl = iconUrl,
        permissions = permissions.toDomain()
    )
}

fun ApiMessage.toDomain(): DomainMessage {
    val domainAuthor = author.toDomain()
    return when (type) {
        ApiMessageType.Default, ApiMessageType.Reply -> {
            val domainAttachments = attachments.map { it.toDomain() }
            val domainEmbeds = embeds.map { it.toDomain() }
            val domainReferencedMessage = referencedMessage?.toDomain()
            DomainMessageRegular(
                id = id.value,
                channelId = channelId.value,
                content = content,
                author = domainAuthor,
                timestamp = timestamp,
                editedTimestamp = editedTimestamp,
                attachments = domainAttachments,
                embeds = domainEmbeds,
                isReply = type == ApiMessageType.Reply,
                referencedMessage = domainReferencedMessage as? DomainMessageRegular
            )
        }
        ApiMessageType.GuildMemberJoin -> {
            DomainMessageMemberJoin(
                id = id.value,
                content = content,
                channelId = channelId.value,
                timestamp = timestamp,
                author = domainAuthor
            )
        }
    }
}

fun ApiMessagePartial.toDomain(): DomainMessageRegularPartial {
    val domainAuthor = author.mapToPartial { it.toDomain() }
    val domainAttachments = attachments.mapToPartial { attachments ->
        attachments.map { it.toDomain() }
    }
    val domainEmbeds = embeds.mapToPartial { embeds ->
        embeds.map { it.toDomain() }
    }
    return DomainMessageRegularPartial(
        id = id.mapToPartial { it.value },
        content = content,
        channelId = channelId.mapToPartial { it.value },
        author = domainAuthor,
        timestamp = timestamp,
        editedTimestamp = editedTimestamp,
        attachments = domainAttachments,
        embeds = domainEmbeds
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
    val permissions = value
    return DomainPermission.values().filter {
        (permissions and it.flags) == it.flags
    }
}

fun ApiEmbed.toDomain(): DomainEmbed {
    val domainFields = fields?.map { it.toDomain() }
    val domainColor = color?.let {
        Color(red = it.red, green = it.green, blue = it.blue)
    }
    val domainAuthor = author?.toDomain()
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = domainColor,
        author = domainAuthor,
        fields = domainFields
    )
}

fun ApiEmbedAuthor.toDomain(): DomainEmbedAuthor {
    return DomainEmbedAuthor(
        name = name
    )
}

fun ApiEmbedField.toDomain(): DomainEmbedField {
    return DomainEmbedField(
        name = name,
        value = value,
    )
}

fun ApiUserSettingsPartial.toDomain(): DomainUserSettingsPartial {
    val domainPartialTheme = theme.mapToPartial { DomainThemeSetting.fromValue(it)!! }
    val domainPartialGuildPositions = guildPositions.mapToPartial { guildPositions ->
        guildPositions.map { it.value }
    }
    val domainPartialStatus = status.mapToPartial { DomainUserStatus.fromValue(it)!! }
    val domainPartialFriendSourceFlags = friendSourceFlags.mapToPartial { it.toDomain() }
    val domainPartialGuildFolders = guildFolders.mapToPartial { guildFolders ->
        guildFolders.map { it.toDomain() }
    }
    val domainPartialCustomStatus = customStatus.mapToPartial { it?.toDomain() }
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
        theme = domainPartialTheme,
        developerMode = developerMode,
        guildPositions = domainPartialGuildPositions,
        detectPlatformAccounts = detectPlatformAccounts,
        status = domainPartialStatus,
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
        friendSourceFlags = domainPartialFriendSourceFlags,
        guildFolders = domainPartialGuildFolders,
        customStatus = domainPartialCustomStatus,
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
        guildPositions = guildPositions.map { it.value },
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
            assets = assets!!.toDomain()
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
            emoji = emoji?.toDomain()
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
