package com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items

import com.nlmk.libs.vkdishestestapp.utils.StringResource

data class DishListItem(
    val id: String,
    val name: StringResource,
    val description: StringResource,
    val price: StringResource,
    val image: String? = null,
    val isChecked: Boolean = false,
    val isEnabled: Boolean = true
) : ListItem
