package com.xinto.opencord.ui.components.attachment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.xinto.opencord.ui.components.OCImage
import com.xinto.opencord.ui.components.OCSize

@Composable
fun AttachmentPicture(
    url: String,
    width: Int,
    height: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit) = {},
) {
    OCImage(
        url = url,
        size = OCSize(width, height),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
    )
}
