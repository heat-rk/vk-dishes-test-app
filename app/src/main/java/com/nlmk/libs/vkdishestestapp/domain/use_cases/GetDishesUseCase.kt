package com.nlmk.libs.vkdishestestapp.domain.use_cases

import com.nlmk.libs.vkdishestestapp.domain.repositories.DishesRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetDishesUseCase @Inject constructor(
    private val dishesRepository: DishesRepository
) {
    suspend fun invoke(offset: Int, limit: Int) =
        dishesRepository.getDishes(offset, limit)
}
