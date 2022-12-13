package com.nlmk.libs.vkdishestestapp.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun LifecycleOwner.repeatOnStart(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}

fun <T> CoroutineScope.launchCollect(flow: Flow<T>, action: suspend (T) -> Unit) = launch {
    flow.collect{action(it)}
}
