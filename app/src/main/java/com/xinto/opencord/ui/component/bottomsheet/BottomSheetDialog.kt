/**
 * Kanged from Android Open Source Dialog.kt
 */

package com.xinto.opencord.ui.component.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

@Immutable
class BottomDialogProperties(
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true
) {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DialogProperties) return false

        if (dismissOnBackPress != other.dismissOnBackPress) return false
        if (dismissOnClickOutside != other.dismissOnClickOutside) return false

        return true
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun hashCode(): Int {
        var result = dismissOnBackPress.hashCode()
        result = 31 * result + dismissOnClickOutside.hashCode()
        return result
    }
}

@Composable
fun BottomSheetDialog(
    onDismissRequest: () -> Unit,
    properties: BottomDialogProperties = BottomDialogProperties(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val composition = rememberCompositionContext()
    val currentContent by rememberUpdatedState(content)
    val dialogId = rememberSaveable { UUID.randomUUID() }
    val bottomDialog = remember {
        BottomSheetDialogWrapper(
            onDismissRequest = onDismissRequest,
            properties = properties,
            composeView = view,
            layoutDirection = layoutDirection,
            density = density,
            dialogId = dialogId
        ).apply {
            setContent(composition) {
                BottomDialogLayout {
                    currentContent()
                }
            }
        }
    }

    DisposableEffect(bottomDialog) {
        bottomDialog.show()

        onDispose {
            bottomDialog.dismiss()
            bottomDialog.disposeComposition()
        }
    }

    SideEffect {
        bottomDialog.updateParameters(
            onDismissRequest = onDismissRequest,
            properties = properties,
            layoutDirection = layoutDirection
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private class BottomSheetDialogWrapper(
    private var onDismissRequest: () -> Unit,
    private var properties: BottomDialogProperties,
    composeView: View,
    layoutDirection: LayoutDirection,
    density: Density,
    dialogId: UUID
) : BottomSheetDialog(composeView.context), ViewRootForInspector {

    private val bottomDialogLayout: BottomDialogLayout

    private val maxSupportedElevation = 30.dp

    override val subCompositionView: AbstractComposeView get() = bottomDialogLayout

    init {
        val window = window ?: error("Dialog has no window")
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        bottomDialogLayout = BottomDialogLayout(context, window).apply {
            tag = "BottomDialog:$dialogId"
            clipChildren = false
            with(density) { elevation = maxSupportedElevation.toPx() }
        }

        fun ViewGroup.disableClipping() {
            clipChildren = false
            if (this is BottomDialogLayout) return
            for (i in 0 until childCount) {
                (getChildAt(i) as? ViewGroup)?.disableClipping()
            }
        }

        (window.decorView as? ViewGroup)?.disableClipping()
        setContentView(bottomDialogLayout)
        ViewTreeLifecycleOwner.set(bottomDialogLayout, ViewTreeLifecycleOwner.get(composeView))
        ViewTreeViewModelStoreOwner.set(bottomDialogLayout, ViewTreeViewModelStoreOwner.get(composeView))
        ViewTreeSavedStateRegistryOwner.set(bottomDialogLayout, ViewTreeSavedStateRegistryOwner.get(composeView))

        setOnDismissListener {
            onDismissRequest()
        }

        setCanceledOnTouchOutside(properties.dismissOnClickOutside)

        updateParameters(onDismissRequest, properties, layoutDirection)
    }

    fun setContent(
        parentComposition: CompositionContext,
        children: @Composable () -> Unit
    ) {
        bottomDialogLayout.setContent(parentComposition, children)
    }

    private fun setLayoutDirection(layoutDirection: LayoutDirection) {
        bottomDialogLayout.layoutDirection = when (layoutDirection) {
            LayoutDirection.Ltr -> android.util.LayoutDirection.LTR
            LayoutDirection.Rtl -> android.util.LayoutDirection.RTL
        }
    }

    fun updateParameters(
        onDismissRequest: () -> Unit,
        properties: BottomDialogProperties,
        layoutDirection: LayoutDirection
    ) {
        this.onDismissRequest = onDismissRequest
        this.properties = properties
        setLayoutDirection(layoutDirection)
    }

    fun disposeComposition() {
        bottomDialogLayout.disposeComposition()
    }

    override fun onBackPressed() {
        if (properties.dismissOnBackPress) {
            onDismissRequest()
        }
    }
}

@SuppressLint("ViewConstructor")
private class BottomDialogLayout(
    context: Context,
    override val window: Window
) : AbstractComposeView(context), DialogWindowProvider {

    private var content: @Composable () -> Unit by mutableStateOf({})

    override var shouldCreateCompositionOnAttachedToWindow: Boolean = false
        private set

    fun setContent(parent: CompositionContext, content: @Composable () -> Unit) {
        setParentCompositionContext(parent)
        this.content = content
        shouldCreateCompositionOnAttachedToWindow = true
        createComposition()
    }

    @Composable
    override fun Content() {
        content()
    }
}

@Composable
private fun BottomDialogLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.fastMap { it.measure(constraints) }
        val width = placeables.fastMaxBy { it.width }?.width ?: constraints.minWidth
        val height = placeables.fastMaxBy { it.height }?.height ?: constraints.minHeight
        layout(width, height) {
            placeables.fastForEach { it.placeRelative(0, 0) }
        }
    }
}