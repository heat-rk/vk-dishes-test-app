package com.nlmk.libs.vkdishestestapp.presentation.screens

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nlmk.libs.vkdishestestapp.converters.ModelConverter
import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.domain.use_cases.DeleteDishesUseCase
import com.nlmk.libs.vkdishestestapp.domain.use_cases.GetDishUseCase
import com.nlmk.libs.vkdishestestapp.domain.use_cases.GetDishesUseCase
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListDishItem
import com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail.DishDetail
import com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail.DishDetailViewModel
import com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list.DishListViewModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class ViewModelsFactoryProvider @Inject constructor(
    private val getDishesUseCase: GetDishesUseCase,
    private val getDishUseCase: GetDishUseCase,
    private val deleteDishesUseCase: DeleteDishesUseCase,
    private val dishToListItemConverter: ModelConverter<Dish, DishesListDishItem>,
    private val dishToDetailConverter: ModelConverter<Dish, DishDetail>,
) {

    object IdKey: CreationExtras.Key<String>

    val factory = viewModelFactory {
        initializer {
            DishListViewModel(
                getDishes = getDishesUseCase,
                deleteDishes = deleteDishesUseCase,
                dishToListItemConverter = dishToListItemConverter,
            )
        }

        initializer {
            DishDetailViewModel(
                id = requireNotNull(get(IdKey)),
                getDish = getDishUseCase,
                dishToDetailConverter = dishToDetailConverter,
            )
        }
    }
}
