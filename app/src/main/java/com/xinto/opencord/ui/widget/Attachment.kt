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
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.size.Size
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
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context)
            .build()
    }
    DisposableEffect(exoPlayer) {
        exoPlayer.setMediaItem(MediaItem.fromUri(url))
        exoPlayer.prepare()
        onDispose {
            exoPlayer.release()
        }
    }
    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                )
                setShowNextButton(false)
                setShowPreviousButton(false)
                controllerShowTimeoutMs = 2000
            }
        },
    )
}