package com.nlmk.libs.vkdishestestapp.converters

import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListDishItem
import com.nlmk.libs.vkdishestestapp.utils.strRes
import ru.heatalways.vkdishestestapp.R
import javax.inject.Inject

class DishToListItemConverter @Inject constructor(): ModelConverter<Dish, DishesListDishItem> {
    override fun convert(source: Dish) = DishesListDishItem(
        id = source.id,
        name = strRes(source.name),
        description = strRes(source.description ?: ""),
        image = source.image,
        price = strRes(R.string.price_format, source.price)
    )
}
