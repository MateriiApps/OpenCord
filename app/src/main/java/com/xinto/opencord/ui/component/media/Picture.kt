package com.xinto.opencord.ui.component.media

import androidx.annotation.Px
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.xinto.opencord.ui.component.image.rememberOpenCordCachePainter

@Composable
fun Picture(
    imageUrl: String,
    @Px imageWidth: Int,
    @Px imageHeight: Int,
    modifier: Modifier = Modifier,
) {
    val picture = rememberOpenCordCachePainter(imageUrl) {
        size(
            width = imageWidth,
            height = imageHeight
        )
    }

    Image(
        modifier = modifier,
        painter = picture,
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}