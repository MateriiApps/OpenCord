package com.xinto.opencord.ui.component

import androidx.compose.runtime.Composable
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest

@Composable
fun rememberOCCoilPainter(
    url: String?,
    builder: ImageRequest.Builder.() -> Unit = {}
) = rememberImagePainter(data = url) {
    diskCachePolicy(CachePolicy.ENABLED)
    builder()
}