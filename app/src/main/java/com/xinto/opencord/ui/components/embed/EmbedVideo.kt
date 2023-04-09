package com.xinto.opencord.ui.components.embed

import android.annotation.SuppressLint
import android.webkit.WebSettings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.xinto.opencord.rest.models.embed.ApiEmbedMedia
import com.xinto.opencord.ui.components.attachment.AttachmentPicture
import com.xinto.opencord.ui.util.ContentAlpha

@Composable
fun EmbedVideo(
    video: ApiEmbedMedia,
    videoPublicUrl: String?,
    thumbnail: ApiEmbedMedia?,
) {
    var pressedLoad by remember { mutableStateOf(false) }
    val webViewState = rememberWebViewState(video.url)

    // For whatever reason there is a sudden temporary jump to Finished state while still loading
    var overHalfLoaded by remember { mutableStateOf(false) }
    val shouldDisplayWebView by remember { derivedStateOf { !webViewState.isLoading && overHalfLoaded } }

    LaunchedEffect(webViewState.loadingState) {
        val state = (webViewState.loadingState as? LoadingState.Loading)
            ?: return@LaunchedEffect

        overHalfLoaded = state.progress >= 0.5f
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.wrapContentSize(),
    ) {
        if ((pressedLoad && !shouldDisplayWebView) || !pressedLoad) {
            // Background thumbnail fill
            if (thumbnail != null && webViewState.lastLoadedUrl == null) {
                AttachmentPicture(
                    url = thumbnail.sizedUrl,
                    width = thumbnail.width ?: 500,
                    height = thumbnail.height ?: 500,
                    modifier = Modifier
                        .heightIn(max = 400.dp),
                )
            } else {
                Surface(
                    content = {},
                    tonalElevation = 5.dp,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .heightIn(max = 400.dp)
                        .aspectRatio(
                            ratio = video.aspectRatio,
                            matchHeightConstraintsFirst = true,
                        ),
                )
            }

            // Pre-WebView action buttons
            Surface(
                shape = MaterialTheme.shapes.small,
                tonalElevation = 12.dp,
                modifier = Modifier.alpha(ContentAlpha.high),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier.padding(6.dp),
                ) {
                    if (webViewState.loadingState is LoadingState.Loading) {
                        CircularProgressIndicator(
                            color = LocalContentColor.current,
                            strokeWidth = 3.dp,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(22.dp),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { pressedLoad = true }
                                .padding(4.dp)
                                .size(30.dp),
                        )

                        if (videoPublicUrl != null) {
                            val uriHandler = LocalUriHandler.current
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable { uriHandler.openUri(videoPublicUrl) }
                                    .padding(4.dp)
                                    .size(26.dp),
                            )
                        }
                    }
                }
            }
        }

        if (pressedLoad) {
            WebView(
                state = webViewState,
                captureBackPresses = false,
                onCreated = { webView ->
                    webView.settings.apply {
                        @SuppressLint("SetJavaScriptEnabled")
                        javaScriptEnabled = true
                        allowFileAccess = false
                        mediaPlaybackRequiresUserGesture = false
                        cacheMode = WebSettings.LOAD_NO_CACHE
                        setGeolocationEnabled(false)
                    }
                },
                modifier = if (shouldDisplayWebView) {
                    Modifier
                        .heightIn(max = 400.dp)
                        .aspectRatio(
                            ratio = video.aspectRatio,
                            matchHeightConstraintsFirst = true,
                        )
                } else {
                    Modifier.size(0.dp)
                },
            )
        }
    }
}
