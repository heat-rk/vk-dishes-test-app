package com.nlmk.libs.vkdishestestapp.converters

import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail.DishDetail
import com.nlmk.libs.vkdishestestapp.utils.strRes
import ru.heatalways.vkdishestestapp.R
import javax.inject.Inject

class DishToDetailConverter @Inject constructor(): ModelConverter<Dish, DishDetail> {
    override fun convert(source: Dish) = DishDetail(
        name = strRes(source.name),
        description = strRes(source.description ?: ""),
        image = source.image,
        price = strRes(R.string.price_format, source.price)
    )
}
