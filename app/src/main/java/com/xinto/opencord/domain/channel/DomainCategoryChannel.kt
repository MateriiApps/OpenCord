package com.xinto.opencord.domain.channel

data class DomainCategoryChannel(
    override val id: Long,
    override val guildId: Long?,
    override val name: String,
    override val position: Int,
    override val parentId: Long? = null,
) : DomainChannel()
