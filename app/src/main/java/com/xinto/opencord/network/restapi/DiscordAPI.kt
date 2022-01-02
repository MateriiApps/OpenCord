package com.xinto.opencord.network.restapi

import com.xinto.opencord.network.body.MessageBody
import com.xinto.opencord.network.response.*
import retrofit2.http.*

interface DiscordAPI {

    @GET("users/@me/guilds")
    suspend fun getMeGuilds(): List<ApiMeGuild>

    @GET("guilds/{guildId}")
    suspend fun getGuild(@Path("guildId") guildId: Long): ApiGuild

    @GET("guilds/{guildId}/channels")
    suspend fun getGuildChannels(@Path("guildId") guildId: Long): List<ApiChannel>

    @GET("channels/{channelId}/messages")
    suspend fun getChannelMessages(@Path("channelId") channelId: Long): List<ApiMessage>

    @POST("channels/{channelId}/messages")
    suspend fun postChannelMessage(
        @Path("channelId") channelId: Long,
        @Body messageBody: MessageBody,
    )
}