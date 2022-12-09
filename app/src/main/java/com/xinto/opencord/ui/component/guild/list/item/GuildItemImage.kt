package com.xinto.opencord.ui.component.guild.list.item

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.component.OCAsyncImage

@Composable
fun GuildItemImage(
    url: String,
    modifier: Modifier = Modifier,
) {
    OCAsyncImage(
        modifier = modifier.size(48.dp),
        url = url,
    )
}
