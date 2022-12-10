package com.xinto.opencord.ui.components.guild.list

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.components.OCAsyncImage

@Composable
fun GuildsListItemImage(
    url: String,
    modifier: Modifier = Modifier,
) {
    OCAsyncImage(
        modifier = modifier.size(48.dp),
        url = url,
    )
}
