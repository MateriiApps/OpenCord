package com.xinto.opencord.ui.widgets.member

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.domain.model.DomainGuildMember
import com.xinto.opencord.ui.component.image.rememberOpenCordCachePainter
import com.xinto.opencord.ui.component.text.Text

@Composable
fun WidgetMember(
    guildMember: DomainGuildMember,
    modifier: Modifier = Modifier
) {
    val memberAvatar = rememberOpenCordCachePainter(guildMember.avatarUrl)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            modifier = Modifier.size(56.dp),
            painter = memberAvatar,
            contentDescription = null,
        )
        Text(
            text = guildMember.nick ?: guildMember.user?.username ?: "",
            style = MaterialTheme.typography.body1
        )
    }
}