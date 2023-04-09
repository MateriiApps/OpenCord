package com.xinto.opencord.ui.screens.home.panels.guild

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.xinto.opencord.domain.guild.DomainGuild
import com.xinto.opencord.ui.components.guild.list.GuildsListHeaderItem
import com.xinto.opencord.ui.components.guild.list.GuildsListItemImage
import com.xinto.opencord.ui.components.guild.list.GuildsListItemText
import com.xinto.opencord.ui.components.guild.list.RegularGuildItem
import kotlinx.collections.immutable.ImmutableList

@Composable
fun GuildsListLoaded(
    onGuildSelect: (Long) -> Unit,
    selectedGuildId: Long,
    guilds: ImmutableList<DomainGuild>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        item {
            RegularGuildItem(
                modifier = Modifier.fillParentMaxWidth(),
                selected = false,
                showIndicator = false,
                onClick = {},
            ) {
                GuildsListHeaderItem()
            }
        }

        item {
            Divider(
                modifier = Modifier
                    .fillParentMaxWidth(0.55f)
                    .padding(bottom = 4.dp, top = 6.dp)
                    .clip(MaterialTheme.shapes.medium),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outline,
            )
        }

        items(guilds) { guild ->
            RegularGuildItem(
                modifier = Modifier.fillParentMaxWidth(),
                selected = selectedGuildId == guild.id,
                showIndicator = true,
                onClick = {
                    onGuildSelect(guild.id)
                },
            ) {
                if (guild.iconUrl != null) {
                    GuildsListItemImage(
                        url = guild.iconUrl,
                    )
                } else {
                    GuildsListItemText(
                        iconText = guild.iconText,
                    )
                }
            }
        }
    }
}
