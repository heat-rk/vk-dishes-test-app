package com.nlmk.libs.vkdishestestapp.di

import com.nlmk.libs.vkdishestestapp.domain.use_cases.*
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
    fun bindFetchDishUseCase(impl: FetchDishUseCaseImpl): FetchDishUseCase

    @Binds
    @Reusable
    fun bindFetchDishesUseCase(impl: FetchDishesUseCaseImpl): FetchDishesUseCase

    @Binds
    @Reusable
    fun bindDeleteDishesUseCase(impl: DeleteDishesUseCaseImpl): DeleteDishesUseCase
}
