package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list

import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishListItem
import com.nlmk.libs.vkdishestestapp.utils.StringResource

data class DishListViewState(
    val isSwipeRefreshEnabled: Boolean = true,

    val isSwipeRefreshing: Boolean = false,

    val isDishesProgressVisible: Boolean = true,

    val isDeleteDishesButtonProgressVisible: Boolean = false,

    val dishes: List<DishListItem> = emptyList(),

    val error: StringResource? = null,
) {
    val isDeleteDishesButtonEnabled: Boolean =
        dishes.any { it.isChecked }
}
