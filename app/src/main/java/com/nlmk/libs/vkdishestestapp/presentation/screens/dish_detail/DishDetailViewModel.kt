package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlmk.libs.vkdishestestapp.converters.ModelConverter
import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.domain.use_cases.GetDishUseCase
import com.nlmk.libs.vkdishestestapp.domain.utils.RequestResult
import com.nlmk.libs.vkdishestestapp.utils.strRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.heatalways.vkdishestestapp.R

class DishDetailViewModel(
    private val id: String,
    private val getDish: GetDishUseCase,
    private val dishToDetailConverter: ModelConverter<Dish, DishDetail>,
): ViewModel() {
    private val _state = MutableStateFlow<DishDetailViewState>(DishDetailViewState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = getDish(id)) {
                is RequestResult.Success -> {
                    _state.value = DishDetailViewState.Ok(
                        body = dishToDetailConverter.convert(result.value)
                    )
                }

                is RequestResult.Failure -> {
                    _state.value = DishDetailViewState.Error(
                        message = strRes(R.string.something_went_wrong)
                    )
                }
            }
        }
    }
}
