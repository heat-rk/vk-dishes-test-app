package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.DishesAdapter
import com.nlmk.libs.vkdishestestapp.utils.getString
import com.nlmk.libs.vkdishestestapp.utils.launchCollect
import com.nlmk.libs.vkdishestestapp.utils.repeatOnStart
import dagger.hilt.android.AndroidEntryPoint
import ru.heatalways.vkdishestestapp.R
import ru.heatalways.vkdishestestapp.databinding.FragmentDishListBinding

@AndroidEntryPoint
class DishListFragment: Fragment(R.layout.fragment_dish_list) {
    private val viewModel: DishListViewModel by viewModels()

    private var _binding: FragmentDishListBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var _dishesAdapter: DishesAdapter? = null
    private val dishesAdapter get() = requireNotNull(_dishesAdapter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDishListBinding.bind(view)

        _dishesAdapter = DishesAdapter(
            onDishClick = { id ->
                viewModel.handleIntent(DishListIntent.OnDishClick(id))
            },
            onDishCheckedChange = { id, isChecked ->
                viewModel.handleIntent(DishListIntent.OnDishCheckedChange(id, isChecked))
            },

            onButtonClick = { id ->
                viewModel.handleIntent(DishListIntent.OnButtonClick(id))
            }
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
        _dishesAdapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun render(state: DishListViewState) {
        binding.errorTextView.isVisible = state.error != null
        binding.dishesRecyclerView.isVisible = state.error == null
        binding.dishesProgressIndicator.isVisible = state.isDishesProgressVisible
        binding.swipeRefreshLayout.isRefreshing = state.isSwipeRefreshing
        binding.swipeRefreshLayout.isEnabled = state.isSwipeRefreshEnabled

        if (state.error != null) {
            binding.errorTextView.text = requireContext().getString(state.error)
        }

        if (state.dishes.isNotEmpty()) {
            dishesAdapter.submitList(state.dishes + state.deleteDishesButton)
        } else {
            dishesAdapter.submitList(state.dishes)
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
        }
    }

    companion object {
        private const val PAGINATION_FACTOR = 1.6f
    }
}