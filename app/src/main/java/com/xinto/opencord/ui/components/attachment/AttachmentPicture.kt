package com.xinto.opencord.ui.component.attachment

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.size.Size
import com.xinto.opencord.ui.component.OCAsyncImage

@Composable
fun AttachmentPicture(
    url: String,
    width: Int,
    height: Int,
    modifier: Modifier = Modifier,
) {
    OCAsyncImage(
        url = url,
        size = Size(width, height),
        modifier = modifier.clip(MaterialTheme.shapes.small),
        contentScale = ContentScale.Fit,
    )
}
