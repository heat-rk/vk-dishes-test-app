package com.nlmk.libs.vkdishestestapp.presentation.screens.dish_detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.bumptech.glide.Glide
import com.nlmk.libs.vkdishestestapp.presentation.screens.ViewModelsFactoryProvider
import com.nlmk.libs.vkdishestestapp.utils.getString
import com.nlmk.libs.vkdishestestapp.utils.launchCollect
import com.nlmk.libs.vkdishestestapp.utils.repeatOnStart
import dagger.hilt.android.AndroidEntryPoint
import ru.heatalways.vkdishestestapp.R
import ru.heatalways.vkdishestestapp.databinding.FragmentDishDetailBinding
import javax.inject.Inject

@AndroidEntryPoint
class DishDetailFragment: Fragment(R.layout.fragment_dish_detail) {
    @Inject
    lateinit var viewModelsFactoryProvider: ViewModelsFactoryProvider

    private val viewModel: DishDetailViewModel by viewModels(
        factoryProducer = { viewModelsFactoryProvider.factory },
        extrasProducer = {
            MutableCreationExtras().apply {
                set(
                    key = ViewModelsFactoryProvider.IdKey,
                    t = requireNotNull(arguments?.getString(ID_KEY))
                )
            }
        }
    )

    private var _binding: FragmentDishDetailBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDishDetailBinding.bind(view)

        viewLifecycleOwner.repeatOnStart {
            launchCollect(viewModel.state, ::render)
        }
    }

    private fun render(state: DishDetailViewState) {
        when (state) {
            is DishDetailViewState.Ok -> renderOkState(state)
            is DishDetailViewState.Error -> renderErrorState(state)
            DishDetailViewState.Loading -> renderLoadingState()
        }
    }

    private fun renderOkState(state: DishDetailViewState.Ok) {
        binding.dishContent.isVisible = true
        binding.dishProgressIndicator.isVisible = false
        binding.errorTextView.isVisible = false

        binding.dishName.text = requireContext().getString(state.body.name)
        binding.dishDescription.text = requireContext().getString(state.body.description)
        binding.dishPrice.text = requireContext().getString(state.body.price)

        Glide.with(requireContext())
            .load(state.body.image)
            .placeholder(R.drawable.ic_dish_placeholder)
            .error(R.drawable.ic_dish_placeholder)
            .into(binding.dishImage)
    }

    private fun renderErrorState(state: DishDetailViewState.Error) {
        binding.dishContent.isInvisible = true
        binding.dishProgressIndicator.isVisible = false
        binding.errorTextView.isVisible = true
        binding.errorTextView.text = requireContext().getString(state.message)
    }

    private fun renderLoadingState() {
        binding.dishContent.isInvisible = true
        binding.dishProgressIndicator.isVisible = true
        binding.errorTextView.isVisible = false
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val ID_KEY = "id"
        const val NAME_KEY = "name"
    }
}
