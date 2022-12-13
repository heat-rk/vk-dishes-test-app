package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlmk.libs.vkdishestestapp.converters.ModelConverter
import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.domain.use_cases.DeleteDishesUseCase
import com.nlmk.libs.vkdishestestapp.domain.use_cases.FetchDishesUseCase
import com.nlmk.libs.vkdishestestapp.domain.utils.RequestResult
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishListItem
import com.nlmk.libs.vkdishestestapp.utils.strRes
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import ru.heatalways.vkdishestestapp.R

class DishListViewModel(
    private val fetchDishesUseCase: FetchDishesUseCase,
    private val deleteDishesUseCase: DeleteDishesUseCase,
    private val dishToListItemConverter: ModelConverter<Dish, DishListItem>,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(DishListViewState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<DishListSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var isPaginationAvailable = false
    private var loadingJob: Job? = null

    init {
        _state.update { state ->
            state.copy(isDishesProgressVisible = true)
        }

        fetchDishes()
    }

    fun handleIntent(intent: DishListIntent) {
        when (intent) {
            DishListIntent.OnScrolledToBottom ->
                onScrolledToLastPage()

            DishListIntent.SwipeRefresh ->
                onSwipeRefresh()

            is DishListIntent.OnDishCheckedChange ->
                onDishCheckedChange(intent.id, intent.isChecked)

            is DishListIntent.OnDishClick ->
                onDishClick(intent.id)

            is DishListIntent.OnButtonClick ->
                onButtonClick(intent.id)
        }
    }

    private fun onDishClick(id: String) = viewModelScope.launch {
        _sideEffects.send(DishListSideEffect.NavigateToDetail(
            id = id,
            name = state.value.dishes.find { it.id == id }?.name ?: strRes("-")
        ))
    }

    private fun onButtonClick(id: String) {
        if (state.value.deleteDishesButton.id == id) {
            onDeleteButtonClick()
        }
    }

    private fun onSwipeRefresh() {
        loadingJob?.cancel()
        loadingJob = null

        isPaginationAvailable = false

        _state.update { state ->
            state.copy(
                dishes = emptyList(),
                isSwipeRefreshing = true
            )
        }

        fetchDishes()
    }

    private fun onScrolledToLastPage() {
        if (isPaginationAvailable) {
            fetchDishes()
        }
    }

    private fun onDishCheckedChange(id: String, isChecked: Boolean) {
        _state.update { state ->
            state.copy(
                dishes = state.dishes.map {
                    if (it.id == id) it.copy(isChecked = isChecked)
                    else it
                }
            )
        }
    }

    private fun onDeleteButtonClick() = viewModelScope.launch {
        setDishesDeleting(true)

        val dishesToDelete = state.value.dishes
            .filter { it.isChecked }
            .map { it.id }

        val result = withContext(ioDispatcher) {
            deleteDishesUseCase.invoke(dishesToDelete)
        }

        when (result) {
            is RequestResult.Success -> {
                _state.update { state ->
                    val dishes = state.dishes.filter { it.id !in result.value }

                    state.copy(
                        dishes = dishes,
                        error = if (dishes.isEmpty()) strRes(R.string.list_empty) else null
                    )
                }

                _sideEffects.send(DishListSideEffect.Message(strRes(R.string.dishes_delete_success)))
            }

            is RequestResult.Failure -> {
                _sideEffects.send(DishListSideEffect.Message(strRes(R.string.something_went_wrong)))
            }
        }

        setDishesDeleting(false)
    }

    private fun fetchDishes() = launchLoadingJob {
        val result = withContext(ioDispatcher) {
            fetchDishesUseCase.invoke(state.value.dishes.size, DISHES_LIMIT)
        }

        when (result) {
            is RequestResult.Success -> {
                _state.update { state ->
                    val dishes =
                        state.dishes + result.value.map { dishToListItemConverter.convert(it) }

                    state.copy(
                        isDishesProgressVisible = false,
                        isSwipeRefreshing = false,
                        dishes = dishes,
                        error = if (dishes.isEmpty()) strRes(R.string.list_empty) else null
                    )
                }

                isPaginationAvailable = result.value.isNotEmpty()
            }

            is RequestResult.Failure -> {
                _state.update { state ->
                    state.copy(
                        isDishesProgressVisible = false,
                        isSwipeRefreshing = false,
                        error = strRes(R.string.something_went_wrong)
                    )
                }
            }
        }
    }

    private fun setDishesDeleting(isDeleting: Boolean) {
        _state.update { state ->
            state.copy(
                dishes = state.dishes.map { it.copy(isEnabled = !isDeleting) },
                deleteDishesButton = state.deleteDishesButton.copy(isProgressVisible = isDeleting),
                isSwipeRefreshEnabled = !isDeleting
            )
        }
    }

    private fun launchLoadingJob(block: suspend CoroutineScope.() -> Unit) {
        loadingJob = viewModelScope.launch {
            block()
            loadingJob = null
        }
    }

    companion object {
        private const val DISHES_LIMIT = 10
    }
}
