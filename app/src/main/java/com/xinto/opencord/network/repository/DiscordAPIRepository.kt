package com.xinto.opencord.network.repository

import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.domain.model.DomainMeGuild
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.network.restapi.DiscordAPI

class DiscordAPIRepository(
    private val discordApi: DiscordAPI
) {

    suspend fun getMeGuilds() =
        discordApi.getMeGuilds().map { apiMeGuild ->
            with (apiMeGuild) {
                DomainMeGuild(
                    id = id.toLong(),
                    name = name,
                    iconUrl = "https://cdn.discordapp.com/icons/$id/$icon"
                )
            }
        }

    suspend fun getGuilds() =
        getMeGuilds().map {
            with (getGuild(it.id)) {
                DomainGuild(
                    id = id,
                    name = name,
                    iconUrl = iconUrl,
                )
            }
        }

    suspend fun getGuild(
        guildId: Long
    ) = with (discordApi.getGuild(guildId)) {
        DomainGuild(
            id = id.toLong(),
            name = name,
            iconUrl = "https://cdn.discordapp.com/icons/$id/$icon",
        )
    }


    suspend fun getGuildChannels(
        guildId: Long
    ) = discordApi.getGuildChannels(guildId).map { apiChannel ->
        DomainChannel.fromApi(apiChannel)
    }

    suspend fun getChannelMessages(
        channelId: Long
    ) = discordApi.getChannelMessages(channelId).map { apiMessage ->
        DomainMessage.fromApi(apiMessage)
    }

}