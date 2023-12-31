package com.xinto.opencord.ui.screens.mentions.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R


@Composable
fun MentionsFilterMenu(
    filterMenuState: SheetState,
    onDismissRequest: () -> Unit,
    includeEveryone: Boolean,
    includeRoles: Boolean,
    includeAllServers: Boolean,
    onToggleEveryone: () -> Unit,
    onToggleRoles: () -> Unit,
    onToggleAllServers: () -> Unit
) {
    ModalBottomSheet(
        sheetState = filterMenuState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 30.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                )

                Text(
                    text = "Mention Filters",
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Divider(thickness = 1.dp)

            val filterItems = remember(includeEveryone, includeRoles, includeAllServers) {
                arrayOf(
                    Triple("Include @everyone mentions", includeEveryone, onToggleEveryone),
                    Triple("Include role mentions", includeRoles, onToggleRoles),
                    Triple("Include mentions from all servers", includeAllServers, onToggleAllServers),
                )
            }

            for ((name, isEnabled, onClick) in filterItems) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(onClick = onClick)
                        .padding(vertical = 2.dp)
                        .padding(start = 6.dp),
                ) {
                    Text(
                        text = name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f, fill = false),
                    )

                    Checkbox(
                        checked = isEnabled,
                        onCheckedChange = { onClick() },
                    )
                }
            }
        }
    }
}

