package com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items

sealed interface DishesListItem {
    val id: String
    val content: String

    fun getPayload(other: DishesListItem): Any? = null
}
