package com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes

import androidx.recyclerview.widget.DiffUtil
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.ButtonListItem
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishListItem
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.ListItem

class DishListItemDiffUtil: DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return if (oldItem is ButtonListItem && newItem is ButtonListItem) {
            oldItem.id == newItem.id
        } else if (oldItem is DishListItem && newItem is DishListItem) {
            oldItem.id == newItem.id
        } else {
            false
        }
    }


    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return if (oldItem is ButtonListItem && newItem is ButtonListItem) {
            oldItem == newItem
        } else if (oldItem is DishListItem && newItem is DishListItem) {
            oldItem == newItem
        } else {
            false
        }
    }

    override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Any? {
        if (oldItem is DishListItem && newItem is DishListItem) {
            if (oldItem.isChecked != newItem.isChecked) {
                return CHECKED_CHANGE
            }
        }

        return super.getChangePayload(oldItem, newItem)
    }

    companion object {
        const val CHECKED_CHANGE = "checked_change"
    }
}
