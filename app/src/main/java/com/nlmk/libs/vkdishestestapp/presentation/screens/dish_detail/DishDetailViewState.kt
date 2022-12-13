package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail

import com.nlmk.libs.vkdishestestapp.utils.StringResource

data class DishDetailViewState(
    val detail: DishDetail = DishDetail(),
    val isLoading: Boolean = true,
    val error: StringResource? = null,
)
