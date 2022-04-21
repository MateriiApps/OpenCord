package com.xinto.opencord.ui.widget

import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.xinto.opencord.ui.component.rememberOCCoilPainter

@Composable
fun WidgetMediaPicture(
    imageUrl: String,
    imageWidth: Int,
    imageHeight: Int,
    modifier: Modifier = Modifier,
) {
    val picture = rememberOCCoilPainter(imageUrl) {
        size(
            width = imageWidth,
            height = imageHeight
        )
    }

    Image(
        modifier = modifier,
        painter = picture,
        contentDescription = null,
        contentScale = ContentScale.Fit
    )
}

@Composable
fun WidgetMediaVideo(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        val exo = ExoPlayer.Builder(context)
            .build()

        exo.addMediaItem(MediaItem.fromUri(videoUrl))

        return@remember exo
    }

    val exoPlayerView = remember {
        PlayerView(context).apply {
            //useController = false
            player = exoPlayer
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            useArtwork = true
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
        }
    }

    DisposableEffect(
        AndroidView(
            modifier = modifier,
            factory = { exoPlayerView },
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}