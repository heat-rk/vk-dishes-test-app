package com.nlmk.libs.vkdishestestapp.presentation.screens

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nlmk.libs.vkdishestestapp.converters.ModelConverter
import com.nlmk.libs.vkdishestestapp.di.annotations.IoDispatcher
import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.domain.use_cases.DeleteDishesUseCase
import com.nlmk.libs.vkdishestestapp.domain.use_cases.FetchDishUseCase
import com.nlmk.libs.vkdishestestapp.domain.use_cases.FetchDishesUseCase
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishListItem
import com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail.DishDetail
import com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail.DishDetailViewModel
import com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list.DishListViewModel
import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@Reusable
class ViewModelsFactoryProvider @Inject constructor(
    private val fetchDishesUseCase: FetchDishesUseCase,
    private val fetchDishUseCase: FetchDishUseCase,
    private val deleteDishesUseCase: DeleteDishesUseCase,
    private val dishToListItemConverter: ModelConverter<Dish, DishListItem>,
    private val dishToDetailConverter: ModelConverter<Dish, DishDetail>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    object IdKey: CreationExtras.Key<String>

    val factory = viewModelFactory {
        initializer {
            DishListViewModel(
                fetchDishesUseCase = fetchDishesUseCase,
                deleteDishesUseCase = deleteDishesUseCase,
                dishToListItemConverter = dishToListItemConverter,
                ioDispatcher = ioDispatcher
            )
        }

        initializer {
            DishDetailViewModel(
                id = requireNotNull(get(IdKey)),
                fetchDishUseCase = fetchDishUseCase,
                dishToDetailConverter = dishToDetailConverter,
                ioDispatcher = ioDispatcher
            )
        }
    }
}
