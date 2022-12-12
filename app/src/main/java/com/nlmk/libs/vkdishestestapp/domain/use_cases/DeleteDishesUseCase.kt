package com.nlmk.libs.vkdishestestapp.domain.use_cases

import com.nlmk.libs.vkdishestestapp.domain.utils.RequestResult

interface DeleteDishesUseCase {
    suspend operator fun invoke(ids: List<String>): RequestResult<List<String>>
}
