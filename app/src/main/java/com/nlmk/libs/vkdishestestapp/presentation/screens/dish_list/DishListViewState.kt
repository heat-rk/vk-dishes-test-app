package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list

import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListDishItem
import com.nlmk.libs.vkdishestestapp.utils.StringResource

sealed interface DishListViewState {
    object SwipeRefreshing: DishListViewState

    object Loading: DishListViewState

    data class Error(val message: StringResource): DishListViewState

    data class Ok(
        val dishes: List<DishesListDishItem> = emptyList(),
        val isDishesDeleting: Boolean = false,
    ): DishListViewState {
        val isDeleteDishesButtonEnabled get() =
            dishes.any { it.isChecked }
    }
}
