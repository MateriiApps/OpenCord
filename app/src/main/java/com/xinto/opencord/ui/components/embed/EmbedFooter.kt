package com.xinto.opencord.ui.components.embed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.components.OCImage
import com.xinto.opencord.ui.components.OCSize

@Composable
fun EmbedFooter(
    text: String,
    iconUrl: String?,
    timestamp: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconUrl != null) {
            OCImage(
                url = iconUrl,
                size = OCSize(64, 64),
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
                memoryCaching = false,
            )
        }

        ProvideTextStyle(MaterialTheme.typography.labelSmall) {
            Text(text)

            if (timestamp != null) {
                Text("•")
                Text(timestamp)
            }
        }
    }
}
