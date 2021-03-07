package com.gallosalocin.caloriecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gallosalocin.caloriecalculator.databinding.ItemFoodBinding
import com.gallosalocin.caloriecalculator.models.FoodWithAllData

class FoodWithAllDataAdapter(
    private val onItemClickListener: (FoodWithAllData) -> Unit,
    private val onItemLongClickListener: (FoodWithAllData) -> Unit,
) : ListAdapter<FoodWithAllData, FoodWithAllDataAdapter.FoodViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentFood = getItem(position)
        holder.bind(currentFood, onItemClickListener, onItemLongClickListener)
    }

    class FoodViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            foodWithCategory: FoodWithAllData,
            onItemClickListener: (FoodWithAllData) -> Unit,
            onItemLongClickListener: (FoodWithAllData) -> Unit,
        ) {

            binding.apply {

                cvFood.setBackgroundColor(foodWithCategory.category.color)
                foodGram.text = foodWithCategory.food.weight.toString()
                foodName.text = foodWithCategory.food.name
                foodCal.text = String.format("%.0f", foodWithCategory.food.calories)
                foodFat.text = String.format("%.1f", foodWithCategory.food.fats)
                foodCarb.text = String.format("%.1f", foodWithCategory.food.carbs)
                foodProt.text = String.format("%.1f", foodWithCategory.food.prots)

                root.setOnClickListener {
                    onItemClickListener.invoke(foodWithCategory)
                }

                root.setOnLongClickListener {
                    onItemLongClickListener(foodWithCategory)
                    true
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FoodWithAllData>() {
        override fun areItemsTheSame(oldItem: FoodWithAllData, newItem: FoodWithAllData) =
            oldItem.food.id == newItem.food.id

        override fun areContentsTheSame(oldItem: FoodWithAllData, newItem: FoodWithAllData) =
            oldItem.food.hashCode() == newItem.food.hashCode()
    }
}