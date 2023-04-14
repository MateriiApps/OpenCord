package com.xinto.opencord.ui.screens

import android.os.Build
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Precision
import com.xinto.opencord.R
import com.xinto.opencord.ui.navigation.ImageViewerData
import com.xinto.opencord.ui.util.ContentAlpha
import com.xinto.opencord.ui.viewmodel.ImageViewerViewModel
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ImageViewerScreen(
    data: ImageViewerData,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImageViewerViewModel = getViewModel(parameters = { parametersOf(data) }),
) {
    var showTopBar by remember { mutableStateOf(true) }
    val topBarOffset by animateFloatAsState(targetValue = if (showTopBar) 0f else -1f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.Black),
    ) {
        TopAppBar(
            title = {
                Column {
                    Text("View image")

                    if (data.fileName != null) {
                        Text(
                            text = data.fileName,
                            style = MaterialTheme.typography.bodyMedium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier
                                .alpha(ContentAlpha.medium)
                                .offset(y = (-2).dp)
                                .padding(bottom = 1.dp),
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.navigation_back),
                    )
                }
            },
            actions = {
                IconButton(onClick = viewModel::downloadImage) {
                    Icon(
                        painter = painterResource(R.drawable.ic_download),
                        contentDescription = "Download image",
                        modifier = Modifier
                            .size(26.dp),
                    )
                }
                IconButton(onClick = viewModel::copyUrlToClipboard) {
                    Icon(
                        painter = painterResource(R.drawable.ic_link),
                        contentDescription = "Copy image link",
                        modifier = Modifier
                            .size(26.dp),
                    )
                }
                IconButton(onClick = viewModel::openInBrowser) {
                    Icon(
                        painter = painterResource(R.drawable.ic_open),
                        contentDescription = "Open in browser",
                        modifier = Modifier
                            .size(24.dp),
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
            ),
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.TopStart)
                .background(Color.Transparent) // Sets absolute container transparent
                .offset { IntOffset(x = 0, y = (64.dp.toPx() * topBarOffset).toInt()) }
                .drawBehind {
                    // Colors background of the part that has an offset
                    drawRect(Color.Black.copy(alpha = 0.8f))
                },
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = { showTopBar = !showTopBar },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ),
        ) {
            ZoomableImage(url = data.url)
        }
    }
}

@Composable
private fun ZoomableImage(
    url: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val model by remember(url) {
        derivedStateOf {
            ImageRequest.Builder(context)
                .data(url)
                .diskCacheKey(url)
                .diskCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.DISABLED)
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

    val zoomState = rememberZoomState(maxScale = 20f)
    var imageLoaded by remember { mutableStateOf(false) }

    if (!imageLoaded) {
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 2.dp,
            modifier = modifier
                .size(18.dp),
        )
    }

    AsyncImage(
        model = model,
        contentDescription = null,
        imageLoader = imageLoader,
        filterQuality = FilterQuality.High,
        contentScale = ContentScale.Inside,
        fallback = painterResource(R.drawable.ic_error),
        onSuccess = { state ->
            zoomState.setContentSize(state.painter.intrinsicSize)
            imageLoaded = true
        },
        modifier = modifier
            .zoomable(zoomState),
    )
}
