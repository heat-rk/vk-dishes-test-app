package com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items

import com.nlmk.libs.vkdishestestapp.utils.StringResource

data class DishesListDishItem(
    override val id: String,
    val name: StringResource,
    val description: StringResource,
    val price: StringResource,
    val image: String? = null,
    val isChecked: Boolean = false,
    val isEnabled: Boolean = true
) : DishesListItem {
    override val content get() = toString()

    override fun getPayload(other: DishesListItem): Any? {
        if (other is DishesListDishItem) {
            if (isChecked != other.isChecked) {
                return CHECKED_CHANGE
            }

            if (isEnabled != other.isEnabled) {
                return ENABLED_CHANGE
            }
        }

        return super.getPayload(other)
    }

    companion object {
        const val CHECKED_CHANGE = "checked_change"
        const val ENABLED_CHANGE = "enabled_change"
    }
}
