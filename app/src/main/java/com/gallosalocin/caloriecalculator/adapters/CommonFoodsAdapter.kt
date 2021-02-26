package com.gallosalocin.caloriecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.data.network.dto.CommonFoods
import com.gallosalocin.caloriecalculator.databinding.ItemCategoryBinding
import com.gallosalocin.caloriecalculator.databinding.ItemCommonFoodBinding

class CommonFoodsAdapter(
    private val onItemClickListener: (CommonFoods) -> Unit
) : ListAdapter<CommonFoods, CommonFoodsAdapter.CommonFoodsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonFoodsViewHolder {
        val binding = ItemCommonFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommonFoodsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommonFoodsViewHolder, position: Int) {
        val currentCommonFood = getItem(position)
        holder.bind(currentCommonFood, onItemClickListener)
    }

    class CommonFoodsViewHolder(private val binding: ItemCommonFoodBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            commonFood: CommonFoods,
            onItemClickListener: (CommonFoods) -> Unit,
        ) {

            binding.apply {

                ivCommonFoodsPhoto.load(commonFood.photo.thumb) {
                    placeholder(R.drawable.ic_placeholder)
                    transformations(CircleCropTransformation())
                    error(R.drawable.ic_image_error)
                }

                tvCommonFoodsName.text = commonFood.foodName

                root.setOnClickListener {
                    onItemClickListener.invoke(commonFood)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CommonFoods>() {
        override fun areItemsTheSame(oldItem: CommonFoods, newItem: CommonFoods) =
            oldItem.foodName == newItem.foodName

        override fun areContentsTheSame(oldItem: CommonFoods, newItem: CommonFoods) =
            oldItem.foodName.hashCode() == newItem.foodName.hashCode()
    }
}