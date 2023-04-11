package com.xinto.opencord.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
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
