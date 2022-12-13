package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list

import com.nlmk.libs.vkdishestestapp.utils.StringResource

sealed interface DishListSideEffect {
    data class Message(val message: StringResource): DishListSideEffect
    data class NavigateToDetail(val id: String, val name: StringResource): DishListSideEffect
}
