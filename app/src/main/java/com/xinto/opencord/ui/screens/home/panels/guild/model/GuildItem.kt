package com.xinto.opencord.ui.screens.home.panels.guild.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import com.xinto.opencord.domain.guild.DomainGuild

@Stable
sealed interface GuildItem {
    val key: Any

    @Immutable
    object Header : GuildItem {
        override val key get() = "HEADER"
    }

    @Immutable
    object Divider : GuildItem {
        override val key get() = "DIVIDER"
    }

    @Stable
    class Guild(
        guild: DomainGuild,
        selected: Boolean = false,
    ) : GuildItem {
        var data by mutableStateOf(guild, neverEqualPolicy())
        var selected by mutableStateOf(selected)

        override val key get() = data.id
    }
}