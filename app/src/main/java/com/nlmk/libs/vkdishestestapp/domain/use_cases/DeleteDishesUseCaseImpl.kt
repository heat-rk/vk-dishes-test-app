package com.nlmk.libs.vkdishestestapp.domain.use_cases

import com.nlmk.libs.vkdishestestapp.domain.repositories.DishesRepository
import javax.inject.Inject

class DeleteDishesUseCaseImpl @Inject constructor(
    private val dishesRepository: DishesRepository
): DeleteDishesUseCase {
    override suspend fun invoke(ids: List<String>) =
        dishesRepository.removeDishes(ids)
}
