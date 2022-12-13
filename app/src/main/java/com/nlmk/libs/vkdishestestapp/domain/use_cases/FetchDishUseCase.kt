package com.nlmk.libs.vkdishestestapp.domain.use_cases

import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.domain.utils.RequestResult

interface FetchDishUseCase {
    suspend operator fun invoke(id: String): RequestResult<Dish>
}
