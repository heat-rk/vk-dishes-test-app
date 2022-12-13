package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail

import com.nlmk.libs.vkdishestestapp.utils.StringResource
import com.nlmk.libs.vkdishestestapp.utils.strRes

data class DishDetail(
    val name: StringResource = strRes("-"),
    val description: StringResource = strRes("-"),
    val price: StringResource = strRes("-"),
    val image: String? = null,
)
