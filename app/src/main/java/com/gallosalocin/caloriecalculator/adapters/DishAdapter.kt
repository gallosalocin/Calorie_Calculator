package com.gallosalocin.caloriecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gallosalocin.caloriecalculator.databinding.ItemDishBinding
import com.gallosalocin.caloriecalculator.models.Dish
import com.gallosalocin.caloriecalculator.models.FoodWithAllData

class DishAdapter(
    private val onItemClickListener: (Dish) -> Unit,
    private val onItemLongClickListener: (Dish) -> Unit,
) : ListAdapter<Dish, DishAdapter.DishViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val binding = ItemDishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val currentDish = getItem(position)
        holder.bind(currentDish, onItemClickListener, onItemLongClickListener)
    }

    class DishViewHolder(private val binding: ItemDishBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            dish: Dish,
            onItemClickListener: (Dish) -> Unit,
            onItemLongClickListener: (Dish) -> Unit,
        ) {

            binding.apply {

                dishName.text = dish.name

                root.setOnClickListener {
                    onItemClickListener.invoke(dish)
                }

                root.setOnLongClickListener {
                    onItemLongClickListener(dish)
                    true
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Dish>() {
        override fun areItemsTheSame(oldItem: Dish, newItem: Dish) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Dish, newItem: Dish) =
            oldItem.id.hashCode() == newItem.id.hashCode()
    }
}