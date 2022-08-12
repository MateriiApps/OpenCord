package com.xinto.opencord.ui.widget

import android.widget.FrameLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.size.Size
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.xinto.opencord.ui.component.OCAsyncImage

@Composable
fun WidgetAttachmentPicture(
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

@Composable
fun WidgetAttachmentVideo(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                addMediaItem(MediaItem.fromUri(url))
                prepare()
            }
    }
    DisposableEffect(
        AndroidView(
            modifier = modifier,
            factory = {
                StyledPlayerView(it).apply {
                    player = exoPlayer
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                    )
                    controllerShowTimeoutMs = 1000
                }
            },
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}