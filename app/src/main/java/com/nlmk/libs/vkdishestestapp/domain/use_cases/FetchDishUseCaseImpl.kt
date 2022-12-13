package com.nlmk.libs.vkdishestestapp.domain.use_cases

import com.nlmk.libs.vkdishestestapp.domain.repositories.DishesRepository
import javax.inject.Inject

class FetchDishUseCaseImpl @Inject constructor(
    private val dishesRepository: DishesRepository
): FetchDishUseCase {
    override suspend fun invoke(id: String) =
        dishesRepository.getDish(id)
}
