package com.nlmk.libs.vkdishestestapp.domain.use_cases

import com.nlmk.libs.vkdishestestapp.domain.repositories.DishesRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class DeleteDishesUseCase @Inject constructor(
    private val dishesRepository: DishesRepository
) {
    suspend fun invoke(ids: List<String>) =
        dishesRepository.removeDishes(ids)
}
