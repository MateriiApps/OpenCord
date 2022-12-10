package com.xinto.opencord.ui.components.attachment

import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@Composable
fun AttachmentVideo(
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
