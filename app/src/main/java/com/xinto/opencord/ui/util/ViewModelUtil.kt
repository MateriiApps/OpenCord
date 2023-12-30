package com.xinto.opencord.ui.util

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.androidx.compose.getViewModel
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

/**
 * Creates and manages a ViewModel bound to the current composable scope.
 * Once the composable goes out of scope, the ViewModel is destroyed.
 */
@OptIn(KoinInternalApi::class)
@Composable
inline fun <reified T : ViewModel> getLocalViewModel(
    qualifier: Qualifier? = null,
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): T {
    val owner = remember {
        object : ViewModelStoreOwner {
            override val viewModelStore = ViewModelStore()
        }
    }

    DisposableEffect(owner) {
        onDispose {
            owner.viewModelStore.clear()
        }
    }

    return getViewModel(
        qualifier = qualifier,
        viewModelStoreOwner = owner,
        scope = scope,
        parameters = parameters,
    )
}

@OptIn(KoinInternalApi::class)
@Composable
inline fun <reified T : ViewModel> findViewModelInTree(
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): T {
    val view = LocalView.current
    val key = "$VIEW_MODEL_BASE_KEY:${T::class.java.canonicalName}"

    val owner = view.findViewTreeViewModelStoreOwner { it.viewModelStore[key] != null }
        ?: error("Unable to find ${T::class.java} in the view tree")

    return getViewModel(
        viewModelStoreOwner = owner,
        scope = scope,
        parameters = parameters,
    )
}

/**
 * Retrieve the first [ViewModelStoreOwner] in the view tree closest to
 * the current View that matches the [predicate].
 */
fun View.findViewTreeViewModelStoreOwner(
    predicate: (ViewModelStoreOwner) -> Boolean
): ViewModelStoreOwner? {
    return generateSequence(this) { v -> v.parent as? View }
        .mapNotNull { v -> v.getTag(VIEW_MODEL_STORE_OWNER_TAG) as? ViewModelStoreOwner }
        .filter(predicate)
        .firstOrNull()
}

@Suppress("INVISIBLE_MEMBER")
const val VIEW_MODEL_BASE_KEY = ViewModelProvider.AndroidViewModelFactory.DEFAULT_KEY
val VIEW_MODEL_STORE_OWNER_TAG = androidx.lifecycle.viewmodel.R.id.view_tree_view_model_store_owner
