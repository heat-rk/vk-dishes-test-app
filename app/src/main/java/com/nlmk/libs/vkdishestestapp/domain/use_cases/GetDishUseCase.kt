package com.nlmk.libs.vkdishestestapp.domain.use_cases

import com.nlmk.libs.vkdishestestapp.domain.repositories.DishesRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetDishUseCase @Inject constructor(
    private val dishesRepository: DishesRepository
) {
    suspend operator fun invoke(id: String) =
        dishesRepository.getDish(id)
}
