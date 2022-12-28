package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlmk.libs.vkdishestestapp.converters.ModelConverter
import com.nlmk.libs.vkdishestestapp.domain.models.Dish
import com.nlmk.libs.vkdishestestapp.domain.use_cases.DeleteDishesUseCase
import com.nlmk.libs.vkdishestestapp.domain.use_cases.GetDishesUseCase
import com.nlmk.libs.vkdishestestapp.domain.utils.RequestResult
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListDishItem
import com.nlmk.libs.vkdishestestapp.utils.strRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.heatalways.vkdishestestapp.R

class DishListViewModel(
    private val getDishes: GetDishesUseCase,
    private val deleteDishes: DeleteDishesUseCase,
    private val dishToListItemConverter: ModelConverter<Dish, DishesListDishItem>,
): ViewModel() {

    private val _state = MutableStateFlow<DishListViewState>(DishListViewState.Loading)
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<DishListSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var isPaginationAvailable = false
    private val loadingJobs = mutableListOf<Job>()

    init {
        fetchDishesNextPage()
    }

    fun handleIntent(intent: DishListIntent) {
        when (intent) {
            DishListIntent.OnScrolledToBottom -> {
                onScrolledToLastPage()
            }

            DishListIntent.SwipeRefresh -> {
                onSwipeRefresh()
            }

            DishListIntent.OnDeleteDishesButtonClick -> {
                onDeleteButtonClick()
            }

            is DishListIntent.OnDishCheckedChange -> {
                onDishCheckedChange(intent.dish, intent.isChecked)
            }

            is DishListIntent.OnDishClick -> {
                onDishClick(intent.dish)
            }
        }
    }

    private fun onDishClick(dish: DishesListDishItem) = viewModelScope.launch {
        _sideEffects.send(DishListSideEffect.NavigateToDetail(
            id = dish.id,
            name = dish.name
        ))
    }

    private fun onSwipeRefresh() {
        loadingJobs.forEach { it.cancel() }
        loadingJobs.clear()
        isPaginationAvailable = false
        _state.value = DishListViewState.SwipeRefreshing
        fetchDishesNextPage()
    }

    private fun onScrolledToLastPage() {
        if (isPaginationAvailable) {
            fetchDishesNextPage()
        }
    }

    private fun onDishCheckedChange(dish: DishesListDishItem, isChecked: Boolean) {
        val state = requireState<DishListViewState.Ok>()

        _state.value = state.copy(
            dishes = state.dishes.map {
                if (it.id == dish.id) it.copy(isChecked = isChecked)
                else it
            }
        )
    }

    private fun onDeleteButtonClick() = viewModelScope.launch {
        val state = requireState<DishListViewState.Ok>()

        _state.value = state.copy(isDishesDeleting = true)

        val dishesToDelete = state.dishes
            .filter { it.isChecked }
            .map { it.id }

        when (val result = deleteDishes(dishesToDelete)) {
            is RequestResult.Success -> {
                val dishes = state.dishes.filter { it.id !in result.value }

                _state.value = if (dishes.isNotEmpty()) {
                    state.copy(
                        dishes = dishes,
                        isDishesDeleting = false
                    )
                } else {
                    DishListViewState.Error(strRes(R.string.list_empty))
                }

                _sideEffects.send(DishListSideEffect.Message(strRes(R.string.dishes_delete_success)))
            }

            is RequestResult.Failure -> {
                _state.value = state.copy(isDishesDeleting = false)
                _sideEffects.send(DishListSideEffect.Message(strRes(R.string.something_went_wrong)))
            }
        }
    }

    private fun fetchDishesNextPage() = launchLoadingJob {
        when (val result = getDishes(
            offset = getAlreadyLoadedDishes().size,
            limit = DISHES_LIMIT
        )) {
            is RequestResult.Success -> {
                val dishes =
                    getAlreadyLoadedDishes() + result.value.map { dishToListItemConverter.convert(it) }

                ensureActive()

                _state.value = if (dishes.isNotEmpty()) {
                    DishListViewState.Ok(dishes = dishes)
                } else {
                    DishListViewState.Error(strRes(R.string.list_empty))
                }

                isPaginationAvailable = result.value.isNotEmpty()
            }

            is RequestResult.Failure -> {
                ensureActive()

                _state.value = DishListViewState.Error(
                    message = strRes(R.string.something_went_wrong)
                )
            }
        }
    }

    private fun getAlreadyLoadedDishes(): List<DishesListDishItem> {
        val state = state.value

        return if (state is DishListViewState.Ok) {
            state.dishes
        } else {
            emptyList()
        }
    }

    private inline fun <reified T: DishListViewState> requireState(): T {
        val state = state.value

        if (state !is T) {
            throw IllegalStateException("${T::class.simpleName} required for this operation!")
        }

        return state
    }

    private fun launchLoadingJob(block: suspend CoroutineScope.() -> Unit) {
        val job = viewModelScope.launch {
            block()
        }

        job.invokeOnCompletion {
            loadingJobs.remove(job)
        }

        loadingJobs.add(job)
    }

    companion object {
        private const val DISHES_LIMIT = 10
    }
}
