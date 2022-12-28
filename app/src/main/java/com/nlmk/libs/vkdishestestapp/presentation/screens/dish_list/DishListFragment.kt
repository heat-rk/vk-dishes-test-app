package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.CompositeAdapter
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.DishesListDishAdapterPartition
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.DishesListButtonAdapterPartition
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishListItemDiffUtil
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListButtonItem
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListItem
import com.nlmk.libs.vkdishestestapp.presentation.screens.ViewModelsFactoryProvider
import com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail.DishDetailFragment
import com.nlmk.libs.vkdishestestapp.utils.getString
import com.nlmk.libs.vkdishestestapp.utils.launchCollect
import com.nlmk.libs.vkdishestestapp.utils.repeatOnStart
import com.nlmk.libs.vkdishestestapp.utils.strRes
import dagger.hilt.android.AndroidEntryPoint
import ru.heatalways.vkdishestestapp.R
import ru.heatalways.vkdishestestapp.databinding.FragmentDishListBinding
import javax.inject.Inject

@AndroidEntryPoint
class DishListFragment: Fragment(R.layout.fragment_dish_list) {

    @Inject
    lateinit var viewModelsFactoryProvider: ViewModelsFactoryProvider

    private val viewModel: DishListViewModel by viewModels(
        factoryProducer = { viewModelsFactoryProvider.factory }
    )

    private var _binding: FragmentDishListBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var _dishesAdapter: CompositeAdapter<DishesListItem>? = null
    private val dishesAdapter get() = requireNotNull(_dishesAdapter)

    private var _navController: NavController? = null
    private val navController get() = requireNotNull(_navController)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDishListBinding.bind(view)
        _navController = findNavController()

        _dishesAdapter = CompositeAdapter(
            diffUtilItemCallback = DishListItemDiffUtil(),
            partitions = arrayOf(
                DishesListDishAdapterPartition(
                    onClick = { dish ->
                        viewModel.handleIntent(DishListIntent.OnDishClick(dish))
                    },
                    onCheckedChange = { dish, isChecked ->
                        viewModel.handleIntent(DishListIntent.OnDishCheckedChange(dish, isChecked))
                    }
                ),

                DishesListButtonAdapterPartition(
                    onClick = { button ->
                        when (button.id) {
                            DELETE_DISHES_BUTTON_ID -> {
                                viewModel.handleIntent(DishListIntent.OnDeleteDishesButtonClick)
                            }
                        }
                    }
                )
            )
        )

        with(binding) {
            val layoutManager = LinearLayoutManager(requireContext())

            dishesRecyclerView.adapter = dishesAdapter
            dishesRecyclerView.layoutManager = layoutManager

            dishesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val itemCount = layoutManager.itemCount
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                    if (lastVisiblePosition >= itemCount / PAGINATION_FACTOR) {
                        viewModel.handleIntent(DishListIntent.OnScrolledToBottom)
                    }
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.handleIntent(DishListIntent.SwipeRefresh)
            }
        }

        viewLifecycleOwner.repeatOnStart {
            launchCollect(viewModel.state, ::render)
            launchCollect(viewModel.sideEffects, ::handleSideEffect)
        }
    }

    override fun onDestroyView() {
        _navController = null
        _dishesAdapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun render(state: DishListViewState) {
        when (state) {
            is DishListViewState.Ok -> renderOkState(state)
            is DishListViewState.Error -> renderErrorState(state)
            DishListViewState.Loading -> renderLoadingState()
            DishListViewState.SwipeRefreshing -> renderSwipeRefreshingState()
        }
    }

    private fun renderSwipeRefreshingState() {
        binding.dishesProgressIndicator.isVisible = false
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun renderLoadingState() {
        binding.errorTextView.isVisible = false
        binding.dishesRecyclerView.isVisible = false
        binding.dishesProgressIndicator.isVisible = true
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun renderErrorState(state: DishListViewState.Error) {
        binding.errorTextView.isVisible = true
        binding.dishesRecyclerView.isVisible = false
        binding.dishesProgressIndicator.isVisible = false
        binding.swipeRefreshLayout.isRefreshing = false
        binding.errorTextView.text = requireContext().getString(state.message)
    }

    private fun renderOkState(state: DishListViewState.Ok) {
        binding.errorTextView.isVisible = false
        binding.dishesRecyclerView.isVisible = true
        binding.dishesProgressIndicator.isVisible = false
        binding.swipeRefreshLayout.isRefreshing = false
        binding.swipeRefreshLayout.isEnabled = !state.isDishesDeleting

        val dishes = state.dishes.map {
            it.copy(
                isEnabled = !state.isDishesDeleting
            )
        }

        if (dishes.isNotEmpty()) {
            val deleteDishesButton = DishesListButtonItem(
                id = DELETE_DISHES_BUTTON_ID,
                text = strRes(R.string.delete_selected),
                isEnabled = state.isDeleteDishesButtonEnabled,
                isProgressVisible = state.isDishesDeleting
            )

            dishesAdapter.submitList(dishes + deleteDishesButton)
        } else {
            dishesAdapter.submitList(dishes)
        }
    }

    private fun handleSideEffect(sideEffect: DishListSideEffect) {
        when (sideEffect) {
            is DishListSideEffect.Message -> {
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(sideEffect.message),
                    Toast.LENGTH_LONG
                ).show()
            }

            is DishListSideEffect.NavigateToDetail -> {
                navController.navigate(
                    resId = R.id.action_dishListFragment_to_dishDetailFragment,
                    args = bundleOf(
                        DishDetailFragment.ID_KEY to sideEffect.id,
                        DishDetailFragment.NAME_KEY to requireContext().getString(sideEffect.name)
                    )
                )
            }
        }
    }

    companion object {
        private const val DELETE_DISHES_BUTTON_ID = "delete_dishes_button"
        private const val PAGINATION_FACTOR = 1.6f
    }
}
