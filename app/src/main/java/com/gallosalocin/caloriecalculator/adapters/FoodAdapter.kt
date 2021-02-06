package com.gallosalocin.caloriecalculator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gallosalocin.caloriecalculator.databinding.ItemFoodBinding
import com.gallosalocin.caloriecalculator.models.FoodWithCategory

class FoodAdapter(
    private val onItemClickListener: (FoodWithCategory) -> Unit
) : ListAdapter<FoodWithCategory, FoodAdapter.FoodViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentFood = getItem(position)
        holder.bind(currentFood, onItemClickListener)
    }

    class FoodViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(foodWithCategory: FoodWithCategory,
                 onItemClickListener: (FoodWithCategory) -> Unit,
        ) {

            binding.apply {

                if (foodWithCategory.category.id == 2) {
                    foodGram.visibility = View.INVISIBLE
                    foodGramSymbol.visibility = View.INVISIBLE
                } else {
                    foodGram.visibility = View.VISIBLE
                    foodGramSymbol.visibility = View.VISIBLE
                    foodGram.text = foodWithCategory.food.weight.toString()
                }
                cvFood.setBackgroundColor(foodWithCategory.category.color)
                foodName.text = foodWithCategory.food.name
                foodCal.text = String.format("%.0f", foodWithCategory.food.calories)
                foodFat.text = String.format("%.1f", foodWithCategory.food.fats)
                foodCarb.text = String.format("%.1f", foodWithCategory.food.carbs)
                foodProt.text = String.format("%.1f", foodWithCategory.food.prots)

                root.setOnClickListener {
                    onItemClickListener.invoke(foodWithCategory)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FoodWithCategory>() {
        override fun areItemsTheSame(oldItem: FoodWithCategory, newItem: FoodWithCategory) =
            oldItem.food.id == newItem.food.id

        override fun areContentsTheSame(oldItem: FoodWithCategory, newItem: FoodWithCategory) =
            oldItem.food.hashCode() == newItem.food.hashCode()
    }
}