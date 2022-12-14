package com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.ButtonListItem
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishListItem
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.ListItem
import com.nlmk.libs.vkdishestestapp.utils.getString
import ru.heatalways.vkdishestestapp.R
import ru.heatalways.vkdishestestapp.databinding.ItemDishBinding
import kotlin.math.roundToInt

class DishesAdapter(
    private val onDishClick: (dish: DishListItem) -> Unit,
    private val onDishCheckedChange: (dish: DishListItem, isChecked: Boolean) -> Unit,
    private val onButtonClick: (button: ButtonListItem) -> Unit
): ListAdapter<ListItem, DishesAdapter.ViewHolder<ListItem>>(DishListItemDiffUtil()) {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<ListItem> {
        return when (viewType) {
            ViewTypes.DISH.ordinal -> {
                ViewHolder.Dish(
                    view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_dish, parent, false),
                    onClick = onDishClick,
                    onCheckedChange = onDishCheckedChange
                ) as ViewHolder<ListItem>
            }

            ViewTypes.BUTTON.ordinal -> {
                ViewHolder.Button(
                    button = MaterialButton(parent.context).apply {
                        layoutParams = ViewGroup.MarginLayoutParams(
                            ViewGroup.MarginLayoutParams.MATCH_PARENT,
                            ViewGroup.MarginLayoutParams.WRAP_CONTENT
                        ).apply {
                            val horizontalMargin = resources.getDimension(R.dimen.inset_default).roundToInt()
                            marginStart = horizontalMargin
                            marginEnd = horizontalMargin
                        }
                    },
                    onClick = onButtonClick
                ) as ViewHolder<ListItem>
            }

            else -> throw IllegalArgumentException("No such view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder<ListItem>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: ViewHolder<ListItem>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.bind(getItem(position), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DishListItem -> ViewTypes.DISH.ordinal
            is ButtonListItem -> ViewTypes.BUTTON.ordinal
        }
    }

    sealed class ViewHolder<I: ListItem>(view: View): RecyclerView.ViewHolder(view) {

        private var _item: I? = null
        protected val item get() = requireNotNull(_item)

        protected val context: Context = view.context

        @CallSuper
        open fun bind(item: I) {
            _item = item
        }

        @CallSuper
        open fun bind(item: I, payloads: List<Any>) {
            _item = item
        }

        class Dish(
            view: View,
            onClick: (dish: DishListItem) -> Unit,
            onCheckedChange: (dish: DishListItem, isChecked: Boolean) -> Unit,
        ): ViewHolder<DishListItem>(view) {

            private val binding = ItemDishBinding.bind(view)

            init {
                binding.root.setOnClickListener {
                    val adapterPosition = adapterPosition

                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onClick(item)
                    }
                }

                binding.dishCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    val adapterPosition = adapterPosition

                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onCheckedChange(item, isChecked)
                    }
                }
            }

            override fun bind(item: DishListItem) {
                super.bind(item)

                Glide.with(context)
                    .load(item.image)
                    .placeholder(R.drawable.ic_dish_placeholder)
                    .error(R.drawable.ic_dish_placeholder)
                    .into(binding.dishImage)

                binding.dishName.text = context.getString(item.name)
                binding.dishPrice.text = context.getString(item.price)
                binding.dishCheckBox.isEnabled = item.isEnabled
                binding.dishCheckBox.isChecked = item.isChecked
            }

            override fun bind(item: DishListItem, payloads: List<Any>) {
                super.bind(item, payloads)

                payloads.forEach { payload ->
                    when (payload) {
                        DishListItemDiffUtil.CHECKED_CHANGE -> {
                            binding.dishCheckBox.isChecked = item.isChecked
                        }

                        DishListItemDiffUtil.ENABLED_CHANGE -> {
                            binding.dishCheckBox.isEnabled = item.isEnabled
                        }
                    }
                }
            }
        }

        class Button(
            private val button: MaterialButton,
            onClick: (button: ButtonListItem) -> Unit
        ): ViewHolder<ButtonListItem>(button) {

            init {
                button.setOnClickListener {
                    val adapterPosition = adapterPosition

                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onClick(item)
                    }
                }
            }

            override fun bind(item: ButtonListItem) {
                super.bind(item)

                button.isEnabled = item.isEnabled && !item.isProgressVisible
                button.setProgressVisibility(item.isProgressVisible)
            }

            private fun MaterialButton.setProgressVisibility(isVisible: Boolean) {
                text = if (isVisible) {
                    context.getString(R.string.loading)
                } else {
                    context.getString(item.text)
                }
            }
        }
    }

    enum class ViewTypes {
        DISH, BUTTON
    }
}
