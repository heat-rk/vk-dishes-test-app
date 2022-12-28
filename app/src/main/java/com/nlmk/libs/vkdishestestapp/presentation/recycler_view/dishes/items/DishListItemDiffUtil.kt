package com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items

import androidx.recyclerview.widget.DiffUtil

class DishListItemDiffUtil: DiffUtil.ItemCallback<DishesListItem>() {
    override fun areItemsTheSame(oldItem: DishesListItem, newItem: DishesListItem) =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: DishesListItem, newItem: DishesListItem) =
        oldItem.content == newItem.content

    override fun getChangePayload(oldItem: DishesListItem, newItem: DishesListItem) =
        oldItem.getPayload(newItem)
}
