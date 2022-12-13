package com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes

import androidx.recyclerview.widget.DiffUtil
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.ButtonListItem
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishListItem
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.ListItem

class DishListItemDiffUtil: DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        if (oldItem is ButtonListItem && newItem is ButtonListItem) {
            return oldItem.id == newItem.id
        }

        if (oldItem is DishListItem && newItem is DishListItem) {
            return oldItem.id == newItem.id
        }

        return false
    }


    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        if (oldItem is ButtonListItem && newItem is ButtonListItem) {
            return oldItem == newItem
        }

        if (oldItem is DishListItem && newItem is DishListItem) {
            return oldItem == newItem
        }

        return false
    }

    override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Any? {
        if (oldItem is DishListItem && newItem is DishListItem) {
            if (oldItem.isChecked != newItem.isChecked) {
                return CHECKED_CHANGE
            }

            if (oldItem.isEnabled != newItem.isEnabled) {
                return ENABLED_CHANGE
            }
        }

        return super.getChangePayload(oldItem, newItem)
    }

    companion object {
        const val CHECKED_CHANGE = "checked_change"
        const val ENABLED_CHANGE = "enabled_change"
    }
}
