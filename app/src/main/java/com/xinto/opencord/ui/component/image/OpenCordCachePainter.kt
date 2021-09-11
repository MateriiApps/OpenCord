package com.xinto.opencord.ui.component.image

import androidx.compose.runtime.Composable
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.CachePolicy

@OptIn(ExperimentalCoilApi::class)
@Composable
fun rememberOpenCordCachePainter(
    url: String?,
) = rememberImagePainter(data = url) {
    diskCachePolicy(CachePolicy.ENABLED)
}