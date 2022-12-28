package com.nlmk.libs.vkdishestestapp.presentation.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class CompositeAdapter<S: Any>(
    diffUtilItemCallback: DiffUtil.ItemCallback<S>,
    private val partitions: Array<AdapterPartition<*, S, *>>
): ListAdapter<S, CompositeAdapter.ViewHolder<S, *>>(diffUtilItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<S, *> {
        val layoutInflater = LayoutInflater.from(parent.context)

        @Suppress("UNCHECKED_CAST")
        return when (val partition = partitions[viewType]) {
            is AdapterPartition.Binding<*, S, *> -> BindingViewHolder(
                binding = partition.createBinding(layoutInflater, parent),
                partition = partition as AdapterPartition.Binding<*, S, ViewBinding>
            )

            is AdapterPartition.Default -> DefaultViewHolder(
                view = partition.createView(layoutInflater, parent),
                partition = partition as AdapterPartition.Default<*, S, View>
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder<S, *>, position: Int) {
        holder.onBind(getItem(position), emptyList())
    }

    override fun onBindViewHolder(
        holder: ViewHolder<S, *>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.onBind(getItem(position), payloads)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        partitions.forEachIndexed { index, partition ->
            if (partition.isItemBelongsToPartition(item, currentList, position)) {
                return index
            }
        }

        throw IllegalArgumentException("No partition for such item [$item], position [$position]")
    }

    sealed class ViewHolder<S : Any, V>(
        protected val view: View,
        protected val partition: AdapterPartition<*, S, V>
    ): RecyclerView.ViewHolder(view) {

        private var _item: S? = null
        protected val item get() = requireNotNull(_item)

        @CallSuper
        open fun onBind(item: S, payloads: List<Any>) {
            _item = item
        }
    }

    class DefaultViewHolder<S : Any>(
        view: View,
        partition: AdapterPartition.Default<*, S, View>
    ): ViewHolder<S, View>(view, partition) {
        init {
            partition.create(
                view = view,
                itemProvider = { item },
                adapterPositionProvider = { adapterPosition }
            )
        }

        override fun onBind(item: S, payloads: List<Any>) {
            super.onBind(item, payloads)
            partition.bind(view, item, payloads)
        }
    }

    class BindingViewHolder<S : Any>(
        private val binding: ViewBinding,
        partition: AdapterPartition.Binding<*, S, ViewBinding>
    ): ViewHolder<S, ViewBinding>(binding.root, partition) {
        init {
            partition.create(
                view = binding,
                itemProvider = { item },
                adapterPositionProvider = { adapterPosition }
            )
        }

        override fun onBind(item: S, payloads: List<Any>) {
            super.onBind(item, payloads)
            partition.bind(binding, item, payloads)
        }
    }
}
