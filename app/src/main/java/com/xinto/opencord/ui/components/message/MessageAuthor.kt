package com.xinto.opencord.ui.components.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.ui.util.ContentAlpha
import com.xinto.opencord.ui.util.ProvideContentAlpha

@Composable
fun MessageAuthor(
    author: String,
    timestamp: String,
    isEdited: Boolean,
    isBot: Boolean,
    modifier: Modifier = Modifier,
    onAuthorClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProvideTextStyle(MaterialTheme.typography.labelLarge) {
            Text(
                text = author,
                modifier = Modifier
                    .clickable(
                        enabled = onAuthorClick != null,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {
                        onAuthorClick?.invoke()
                    },
            )
        }
        if (isBot) {
            ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                Surface(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = MaterialTheme.shapes.extraSmall,
                ) {
                    Text(
                        text = stringResource(R.string.chat_bot_label),
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                }
            }
        }
        ProvideContentAlpha(ContentAlpha.low) {
            Text("·")
            ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                Text(timestamp)
            }
            if (isEdited) {
                Text("·")
                Icon(
                    modifier = Modifier
                        .size(12.dp),
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                )
            }
        }
    }
}
