package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list

sealed interface DishListIntent {
    object SwipeRefresh: DishListIntent
    object OnScrolledToBottom: DishListIntent
    data class OnButtonClick(val id: String): DishListIntent
    data class OnDishClick(val id: String): DishListIntent
    data class OnDishCheckedChange(val id: String, val isChecked: Boolean): DishListIntent
}
