package com.xinto.opencord.ui.screens.home.panels.guild

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.guild.DomainGuild
import com.xinto.opencord.ui.components.guild.list.GuildsListItemImage
import com.xinto.opencord.ui.components.guild.list.GuildsListItemText
import com.xinto.opencord.ui.components.guild.list.RegularGuildItem

@Composable
fun GuildsListLoaded(
    onGuildSelect: (Long) -> Unit,
    selectedGuildId: Long,
    guilds: List<DomainGuild>,
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
                GuildsListItemText {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_discord_logo),
                        contentDescription = stringResource(R.string.guilds_home),
                    )
                }
            }
        }

        item {
            Divider(
                modifier = Modifier
                    .fillParentMaxWidth(0.55f)
                    .padding(bottom = 4.dp)
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
                    GuildsListItemText {
                        Text(guild.iconText)
                    }
                }
            }
        }
    }
}
