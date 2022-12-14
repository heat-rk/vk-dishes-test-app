package com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items

import com.nlmk.libs.vkdishestestapp.utils.StringResource

data class ButtonListItem(
    val id: String,
    val text: StringResource,
    val isProgressVisible: Boolean = false,
    val isEnabled: Boolean = true,
): ListItem
