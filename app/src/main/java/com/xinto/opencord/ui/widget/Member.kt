package com.xinto.opencord.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.domain.model.DomainGuildMember
import com.xinto.opencord.ui.component.OCAsyncImage

@Composable
fun WidgetMemberListItem(
    guildMember: DomainGuildMember,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OCAsyncImage(
            modifier = Modifier.size(56.dp),
            url = guildMember.avatarUrl,
        )
        Text(
            text = guildMember.nick ?: guildMember.user?.username ?: "",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
