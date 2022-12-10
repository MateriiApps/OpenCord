package com.xinto.opencord.domain.channel

data class DomainTextChannel(
    override val id: Long,
    override val guildId: Long?,
    override val name: String,
    override val position: Int,
    override val parentId: Long?,
    val nsfw: Boolean,
) : DomainChannel()
