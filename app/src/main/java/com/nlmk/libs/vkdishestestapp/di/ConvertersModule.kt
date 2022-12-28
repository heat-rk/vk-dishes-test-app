package com.nlmk.libs.vkdishestestapp.di

import com.nlmk.libs.vkdishestestapp.converters.DishToDetailConverter
import com.nlmk.libs.vkdishestestapp.converters.DishToListItemConverter
import com.nlmk.libs.vkdishestestapp.converters.ModelConverter
import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListDishItem
import com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail.DishDetail
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ConvertersModule {
    @Binds
    @Reusable
    fun bindDishToListItemConverter(
        impl: DishToListItemConverter
    ): ModelConverter<Dish, DishesListDishItem>

    @Binds
    @Reusable
    fun bindDishToDetailConverter(
        impl: DishToDetailConverter
    ): ModelConverter<Dish, DishDetail>
}
