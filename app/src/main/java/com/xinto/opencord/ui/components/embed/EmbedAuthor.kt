package com.xinto.opencord.ui.components.embed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.components.OCImage
import com.xinto.opencord.ui.components.OCSize

@Composable
fun EmbedAuthor(
    name: String,
    url: String?,
    iconUrl: String?,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        if (iconUrl != null) {
            OCImage(
                url = iconUrl,
                memoryCaching = false,
                size = OCSize(64, 64),
                modifier = Modifier
                    .size(18.dp)
                    .clip(MaterialTheme.shapes.extraSmall),
            )
        }

        ClickableText(
            text = buildAnnotatedString { append(name) },
            style = MaterialTheme.typography.labelMedium.copy(
                textDecoration = if (url != null) TextDecoration.Underline else null,
                color = if (url != null) MaterialTheme.colorScheme.primary else LocalContentColor.current,
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            onClick = {
                if (url != null) {
                    uriHandler.openUri(url)
                }
            },
        )
    }
}
