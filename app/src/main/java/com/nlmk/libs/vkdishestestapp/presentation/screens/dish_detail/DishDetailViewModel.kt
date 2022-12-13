package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlmk.libs.vkdishestestapp.converters.ModelConverter
import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.domain.use_cases.FetchDishUseCase
import com.nlmk.libs.vkdishestestapp.domain.utils.RequestResult
import com.nlmk.libs.vkdishestestapp.utils.strRes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.heatalways.vkdishestestapp.R

class DishDetailViewModel(
    private val id: String,
    private val fetchDishUseCase: FetchDishUseCase,
    private val dishToDetailConverter: ModelConverter<Dish, DishDetail>,
    private val ioDispatcher: CoroutineDispatcher,
): ViewModel() {
    private val _state = MutableStateFlow(DishDetailViewState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { fetchDishUseCase.invoke(id) }) {
                is RequestResult.Success -> {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            detail = dishToDetailConverter.convert(result.value)
                        )
                    }
                }

                is RequestResult.Failure -> {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            error = strRes(R.string.something_went_wrong)
                        )
                    }
                }
            }
        }
    }
}
