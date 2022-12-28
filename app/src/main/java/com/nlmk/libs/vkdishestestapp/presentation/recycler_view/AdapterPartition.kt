package com.nlmk.libs.vkdishestestapp.presentation.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

sealed class AdapterPartition<T: S, S: Any, V> {
    fun bind(view: V, item: S, payloads: List<Any>) {
        @Suppress("UNCHECKED_CAST")
        onBind(view, item as T, payloads)
    }

    fun create(view: V, itemProvider: () -> S, adapterPositionProvider: () -> Int) {
        @Suppress("UNCHECKED_CAST")
        onCreateViewHolder(view, itemProvider as () -> T, adapterPositionProvider)
    }

    abstract fun isItemBelongsToPartition(item: S, items: List<S>, position: Int): Boolean

    abstract fun onCreateViewHolder(view: V, itemProvider: () -> T, adapterPositionProvider: () -> Int)

    abstract fun onBind(view: V, item: T, payloads: List<Any>)

    abstract fun createView(layoutInflater: LayoutInflater, root: ViewGroup): View

    @Suppress("UnnecessaryAbstractClass")
    abstract class Default<T: S, S: Any, V: View>: AdapterPartition<T, S, V>()

    abstract class Binding<T: S, S: Any, V: ViewBinding>: AdapterPartition<T, S, V>() {
        final override fun createView(layoutInflater: LayoutInflater, root: ViewGroup) =
            createBinding(layoutInflater, root).root

        abstract fun createBinding(layoutInflater: LayoutInflater, root: ViewGroup): V
    }
}
