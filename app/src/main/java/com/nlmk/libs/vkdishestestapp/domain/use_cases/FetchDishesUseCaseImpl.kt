package com.nlmk.libs.vkdishestestapp.domain.use_cases

import com.nlmk.libs.vkdishestestapp.domain.repositories.DishesRepository
import javax.inject.Inject

class FetchDishesUseCaseImpl @Inject constructor(
    private val dishesRepository: DishesRepository
): FetchDishesUseCase {
    override suspend fun invoke(offset: Int, limit: Int) =
        dishesRepository.getDishes(offset, limit)
}
