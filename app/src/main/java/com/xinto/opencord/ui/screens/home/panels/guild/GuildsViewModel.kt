package com.xinto.opencord.ui.screens.home.panels.guild

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.store.GuildStore
import com.xinto.opencord.store.PersistentDataStore
import com.xinto.opencord.store.fold
import com.xinto.opencord.ui.screens.home.panels.guild.model.GuildItem
import com.xinto.opencord.util.collectIn
import com.xinto.opencord.util.removeFirst
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@Stable
class GuildsViewModel(
    guildStore: GuildStore,
    private val persistentDataStore: PersistentDataStore,
) : ViewModel() {
    var state by mutableStateOf<HomeGuildPanelState>(HomeGuildPanelState.Loading, referentialEqualityPolicy())
        private set

    // List of all items to display (not just guilds)
    val listItems = mutableStateListOf<GuildItem>()

    // Dual reference to all Guild items in listItems for performance
    private val guildItemRefs = mutableMapOf<Long, GuildItem.Guild>()

    fun selectGuild(guildId: Long) {
        viewModelScope.launch {
            persistentDataStore.updateCurrentGuild(guildId)

            guildItemRefs.values
                .find { it.selected }
                ?.selected = false

            guildItemRefs[guildId]?.selected = true
        }
    }

    init {
        persistentDataStore.observeCurrentGuild()
            .onStart {
                val guilds = guildStore.fetchGuilds() // TODO: don't do anything w/o cache (keep Loading state)
                val items = guilds.map {
                    GuildItem.Guild(
                        guild = it,
                        selected = false,
                    )
                }

                guildItemRefs.putAll(items.map { it.data.id to it })
                listItems.add(GuildItem.Header)
                listItems.add(GuildItem.Divider)
                listItems.addAll(items)
                state = HomeGuildPanelState.Loaded
            }
            .collectIn(viewModelScope) { guildId ->
                guildItemRefs.values
                    .find { it.selected }
                    ?.selected = false

                guildItemRefs[guildId]?.selected = true
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

            state = HomeGuildPanelState.Loaded // Will have an effect on no-cache app load
        }
    }

}
