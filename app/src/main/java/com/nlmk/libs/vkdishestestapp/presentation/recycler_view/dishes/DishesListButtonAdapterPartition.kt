package com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.AdapterPartition
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListButtonItem
import com.nlmk.libs.vkdishestestapp.presentation.recycler_view.dishes.items.DishesListItem
import com.nlmk.libs.vkdishestestapp.utils.getString
import ru.heatalways.vkdishestestapp.R
import kotlin.math.roundToInt

class DishesListButtonAdapterPartition(
    private val onClick: (button: DishesListButtonItem) -> Unit
): AdapterPartition.Default<DishesListButtonItem, DishesListItem, Button>() {
    override fun isItemBelongsToPartition(
        item: DishesListItem,
        items: List<DishesListItem>,
        position: Int
    ): Boolean {
        return item is DishesListButtonItem
    }

    override fun createView(layoutInflater: LayoutInflater, root: ViewGroup): View {
        return MaterialButton(root.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            ).apply {
                val horizontalMargin = resources.getDimension(R.dimen.inset_default).roundToInt()
                marginStart = horizontalMargin
                marginEnd = horizontalMargin
            }
        }
    }

    override fun onCreateViewHolder(
        view: Button,
        itemProvider: () -> DishesListButtonItem,
        adapterPositionProvider: () -> Int
    ) {
        view.setOnClickListener {
            if (adapterPositionProvider() != RecyclerView.NO_POSITION) {
                onClick(itemProvider())
            }
        }
    }

    override fun onBind(view: Button, item: DishesListButtonItem, payloads: List<Any>) {
        val context = view.context

        view.isEnabled = item.isEnabled && !item.isProgressVisible

        view.text = if (item.isProgressVisible) {
            context.getString(R.string.loading)
        } else {
            context.getString(item.text)
        }
    }
}
