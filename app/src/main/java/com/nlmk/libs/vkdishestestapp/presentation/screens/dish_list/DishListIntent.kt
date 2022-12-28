package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list

import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListDishItem

sealed interface DishListIntent {
    object SwipeRefresh: DishListIntent
    object OnScrolledToBottom: DishListIntent
    object OnDeleteDishesButtonClick: DishListIntent
    data class OnDishClick(val dish: DishesListDishItem): DishListIntent
    data class OnDishCheckedChange(val dish: DishesListDishItem, val isChecked: Boolean): DishListIntent
}
