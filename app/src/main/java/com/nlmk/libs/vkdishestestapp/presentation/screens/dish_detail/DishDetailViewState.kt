package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail

import com.nlmk.libs.vkdishestestapp.utils.StringResource

sealed interface DishDetailViewState {
    object Loading: DishDetailViewState
    data class Error(val message: StringResource): DishDetailViewState
    data class Ok(val body: DishDetail): DishDetailViewState
}
