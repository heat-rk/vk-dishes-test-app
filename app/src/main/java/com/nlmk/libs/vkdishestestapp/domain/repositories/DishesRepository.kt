package com.nlmk.libs.vkdishestestapp.domain.repositories

import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.domain.utils.RequestResult

interface DishesRepository {
    suspend fun getDishes(offset: Int, limit: Int): RequestResult<List<Dish>>
    suspend fun getDish(id: String): RequestResult<Dish>
    suspend fun removeDishes(ids: List<String>): RequestResult<List<String>>
}
