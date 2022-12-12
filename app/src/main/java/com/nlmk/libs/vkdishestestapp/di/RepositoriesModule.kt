package com.nlmk.libs.vkdishestestapp.di

import com.nlmk.libs.vkdishestestapp.data.repositories.DishesRepositoriesImpl
import com.nlmk.libs.vkdishestestapp.domain.repositories.DishesRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {
    @Binds
    @Reusable
    fun bindDishesRepository(impl: DishesRepositoriesImpl): DishesRepository
}
