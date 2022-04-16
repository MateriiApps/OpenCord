package com.xinto.opencord.ui.navigation

import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier

@Composable
fun <T : Parcelable> OCNavigation(
    navigator: OCNavigator<T>,
    modifier: Modifier = Modifier,
    transitionSpec: AnimatedContentScope<T>.() -> ContentTransform,
    backPressEnabled: Boolean,
    onBackPress: () -> Unit,
    content: @Composable (T) -> Unit,
) {
    val saveableStateHolder = rememberSaveableStateHolder()
    BackHandler(
        enabled = backPressEnabled,
        onBack = onBackPress
    )
    AnimatedContent(
        modifier = modifier,
        targetState = navigator.current,
        transitionSpec = transitionSpec
    ) { animatedCurrentItem ->
        saveableStateHolder.SaveableStateProvider(animatedCurrentItem) {
            content(animatedCurrentItem)
        }
    }
}