package com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items

import com.nlmk.libs.vkdishestestapp.utils.StringResource

data class DishesListButtonItem(
    override val id: String,
    val text: StringResource,
    val isProgressVisible: Boolean = false,
    val isEnabled: Boolean = true,
): DishesListItem {
    override val content get() = toString()
}
