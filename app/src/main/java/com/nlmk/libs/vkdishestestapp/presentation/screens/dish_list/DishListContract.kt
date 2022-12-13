package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list

import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.ButtonListItem
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishListItem
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.ListItem
import com.nlmk.libs.vkdishestestapp.utils.StringResource
import com.nlmk.libs.vkdishestestapp.utils.strRes
import ru.heatalways.vkdishestestapp.R

data class DishListViewState(
    val isSwipeRefreshing: Boolean = false,
    val isDishesProgressVisible: Boolean = true,
    val isSwipeRefreshEnabled: Boolean = true,
    val dishes: List<DishListItem> = emptyList(),
    val deleteDishesButton: ButtonListItem = ButtonListItem(
        id = "delete_dishes_button",
        text = strRes(R.string.delete_selected)
    ),
    val error: StringResource? = null,
)

sealed interface DishListIntent {
    object SwipeRefresh: DishListIntent
    object OnScrolledToBottom: DishListIntent
    data class OnButtonClick(val id: String): DishListIntent
    data class OnDishClick(val id: String): DishListIntent
    data class OnDishCheckedChange(val id: String, val isChecked: Boolean): DishListIntent
}

sealed interface DishListSideEffect {
    data class Message(val message: StringResource): DishListSideEffect
}
