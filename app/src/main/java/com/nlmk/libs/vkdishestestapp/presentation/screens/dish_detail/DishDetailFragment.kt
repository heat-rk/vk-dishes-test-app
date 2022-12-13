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
        binding.dishContent.isInvisible = state.isLoading || state.error != null
        binding.dishProgressIndicator.isVisible = state.isLoading
        binding.errorTextView.isVisible = state.error != null

        binding.errorTextView.text = requireContext().getString(state.error)
        binding.dishName.text = requireContext().getString(state.detail.name)
        binding.dishDescription.text = requireContext().getString(state.detail.description)
        binding.dishPrice.text = requireContext().getString(state.detail.price)

        Glide.with(requireContext())
            .load(state.detail.image)
            .placeholder(R.drawable.ic_dish_placeholder)
            .error(R.drawable.ic_dish_placeholder)
            .into(binding.dishImage)
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
