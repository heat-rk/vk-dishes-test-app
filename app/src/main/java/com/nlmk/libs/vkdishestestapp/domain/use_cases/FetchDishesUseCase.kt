package com.nlmk.libs.vkdishestestapp.domain.use_cases

import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.domain.utils.RequestResult

interface FetchDishesUseCase {
    suspend operator fun invoke(offset: Int, limit: Int): RequestResult<List<Dish>>
}
