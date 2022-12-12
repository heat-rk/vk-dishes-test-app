package com.nlmk.libs.vkdishestestapp.di

import com.nlmk.libs.vkdishestestapp.domain.use_cases.DeleteDishesUseCase
import com.nlmk.libs.vkdishestestapp.domain.use_cases.DeleteDishesUseCaseImpl
import com.nlmk.libs.vkdishestestapp.domain.use_cases.FetchDishesUseCase
import com.nlmk.libs.vkdishestestapp.domain.use_cases.FetchDishesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCasesModule {
    @Binds
    @Reusable
    fun bindFetchDishesUseCase(impl: FetchDishesUseCaseImpl): FetchDishesUseCase

    @Binds
    @Reusable
    fun bindDeleteDishesUseCase(impl: DeleteDishesUseCaseImpl): DeleteDishesUseCase
}
