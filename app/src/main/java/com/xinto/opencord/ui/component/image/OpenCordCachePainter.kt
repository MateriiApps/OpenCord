package com.xinto.opencord.ui.component.image

import androidx.compose.runtime.Composable
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest

@OptIn(ExperimentalCoilApi::class)
@Composable
fun rememberOpenCordCachePainter(
    url: String?,
    builder: ImageRequest.Builder.() -> Unit = {}
) = rememberImagePainter(data = url) {
    diskCachePolicy(CachePolicy.ENABLED)
    builder()
}