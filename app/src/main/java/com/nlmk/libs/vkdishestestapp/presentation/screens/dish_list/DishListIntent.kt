package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list

import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishListItem

sealed interface DishListIntent {
    object SwipeRefresh: DishListIntent
    object OnScrolledToBottom: DishListIntent
    object OnDeleteDishesButtonClick: DishListIntent
    data class OnDishClick(val dish: DishListItem): DishListIntent
    data class OnDishCheckedChange(val dish: DishListItem, val isChecked: Boolean): DishListIntent
}
