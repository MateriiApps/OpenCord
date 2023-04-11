package com.xinto.opencord.ui.components

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Precision
import coil.size.Size
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
private val IMAGE_DISPATCHER = Dispatchers.IO.limitedParallelism(5)

@Composable
fun OCImage(
    url: String?,
    modifier: Modifier = Modifier,
    size: OCSize = OCSize.ORIGINAL,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    memoryCaching: Boolean = false,
) {
    val context = LocalContext.current

    val model by remember(url, size, memoryCaching) {
        derivedStateOf {
            ImageRequest.Builder(context)
                .data(url)
                .dispatcher(IMAGE_DISPATCHER)
                .diskCacheKey(url)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(if (memoryCaching) CachePolicy.ENABLED else CachePolicy.READ_ONLY)
                .memoryCacheKey(url)
                .size(size.size ?: Size.ORIGINAL)
                .precision(Precision.EXACT)
                .crossfade(true)
                .build()
        }
    }
    val imageLoader by remember {
        derivedStateOf {
            ImageLoader.Builder(context).components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }.build()
        }
    }

    SubcomposeAsyncImage(
        model = model,
        contentDescription = null,
        imageLoader = imageLoader,
        modifier = modifier,
        loading = {
            Box(
                modifier = modifier
                    .placeholder(
                        visible = true,
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            elevation = LocalAbsoluteTonalElevation.current + 2.dp,
                        ),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                elevation = LocalAbsoluteTonalElevation.current + 3.dp,
                            ),
                        ),
                    )
                    .fillMaxSize(),
            )
        },
        success = {
            SubcomposeAsyncImageContent()
        },
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
    )
}

@JvmInline
@Immutable
value class OCSize(val size: Size?) {
    constructor(width: Int, height: Int)
            : this(Size(width, height))

    companion object {
        val ORIGINAL = OCSize(Size.ORIGINAL)
    }
}
