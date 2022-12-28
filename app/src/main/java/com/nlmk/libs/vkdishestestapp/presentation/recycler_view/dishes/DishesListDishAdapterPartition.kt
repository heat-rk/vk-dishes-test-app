package com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.AdapterPartition
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListDishItem
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListItem
import com.nlmk.libs.vkdishestestapp.utils.getString
import ru.heatalways.vkdishestestapp.R
import ru.heatalways.vkdishestestapp.databinding.ItemDishBinding

class DishesListDishAdapterPartition(
    private val onClick: (dish: DishesListDishItem) -> Unit,
    private val onCheckedChange: (dish: DishesListDishItem, isChecked: Boolean) -> Unit
): AdapterPartition.Binding<DishesListDishItem, DishesListItem, ItemDishBinding>() {
    override fun isItemBelongsToPartition(
        item: DishesListItem,
        items: List<DishesListItem>,
        position: Int
    ): Boolean {
        return item is DishesListDishItem
    }

    override fun createBinding(layoutInflater: LayoutInflater, root: ViewGroup): ItemDishBinding {
        return ItemDishBinding.inflate(layoutInflater, root, false)
    }

    override fun onCreateViewHolder(
        view: ItemDishBinding,
        itemProvider: () -> DishesListDishItem,
        adapterPositionProvider: () -> Int
    ) {
        view.root.setOnClickListener {
            if (adapterPositionProvider() != RecyclerView.NO_POSITION) {
                onClick(itemProvider())
            }
        }

        view.dishCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (adapterPositionProvider() != RecyclerView.NO_POSITION) {
                onCheckedChange(itemProvider(), isChecked)
            }
        }
    }

    override fun onBind(view: ItemDishBinding, item: DishesListDishItem, payloads: List<Any>) {
        val context = view.root.context

        if (payloads.isEmpty()) {
            Glide.with(context)
                .load(item.image)
                .placeholder(R.drawable.ic_dish_placeholder)
                .error(R.drawable.ic_dish_placeholder)
                .into(view.dishImage)

            view.dishName.text = context.getString(item.name)
            view.dishPrice.text = context.getString(item.price)
            view.dishCheckBox.isEnabled = item.isEnabled
            view.dishCheckBox.isChecked = item.isChecked
        } else {
            payloads.forEach { payload ->
                when (payload) {
                    DishesListDishItem.CHECKED_CHANGE -> {
                        view.dishCheckBox.isChecked = item.isChecked
                    }

                    DishesListDishItem.ENABLED_CHANGE -> {
                        view.dishCheckBox.isEnabled = item.isEnabled
                    }
                }
            }
        }
    }
}
