package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.guild.DomainGuild
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.store.GuildStore
import com.xinto.opencord.store.fold
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import com.xinto.opencord.util.collectIn
import com.xinto.opencord.util.removeFirst
import kotlinx.coroutines.launch

@Stable
class GuildsViewModel(
    guildStore: GuildStore,
    persistentDataManager: PersistentDataManager,
) : BasePersistenceViewModel(persistentDataManager) {
    var state by mutableStateOf<State>(State.Loading, referentialEqualityPolicy())
        private set

    // List of all items to display (not just guilds)
    val listItems = mutableStateListOf<GuildItem>()

    // Dual reference to all Guild items in listItems for performance
    private val guildItemRefs = mutableMapOf<Long, GuildItem.Guild>()

    fun selectGuild(guildId: Long) {
        persistentGuildId = guildId

        guildItemRefs.values
            .find { it.selected }
            ?.selected = false

        guildItemRefs[guildId]?.selected = true
    }

    init {
        viewModelScope.launch {
            val guilds = guildStore.fetchGuilds() // TODO: don't do anything w/o cache (keep Loading state)
            val items = guilds.map {
                GuildItem.Guild(
                    guild = it,
                    selected = persistentGuildId == it.id,
                )
            }

            guildItemRefs.putAll(items.asSequence().map { it.data.id to it })
            listItems.add(GuildItem.Header)
            listItems.add(GuildItem.Divider)
            listItems.addAll(items)
            state = State.Loaded
        }

        guildStore.observeGuilds().collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = {
                    val existingItem = guildItemRefs[it.id]

                    if (existingItem != null) {
                        existingItem.data = it
                    } else {
                        val item = GuildItem.Guild(it)
                        listItems.add(item)
                        guildItemRefs[it.id] = item
                    }
                },
                onUpdate = { guildItemRefs[it.id]?.data = it },
                onDelete = { guildId ->
                    val item = guildItemRefs.remove(guildId)
                        ?: return@fold

                    // Guaranteed if item retrieved
                    listItems.removeFirst { it === item }
                },
            )

            state = State.Loaded // Will have an effect on no-cache app load
        }
    }

    sealed interface State {
        object Loading : State
        object Loaded : State
        object Error : State
    }

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
}
