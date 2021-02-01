package com.gallosalocin.caloriecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gallosalocin.caloriecalculator.databinding.ItemCategoryBinding
import com.gallosalocin.caloriecalculator.models.Category

class CategoryAdapter(
    private val onItemClickListener: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCategory = getItem(position)
        holder.bind(currentCategory, onItemClickListener)
    }

    class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            category: Category,
            onItemClickListener: (Category) -> Unit,
        ) {

            binding.apply {

                cvCategory.setBackgroundColor(category.color)
                categoryName.text = category.name

                root.setOnClickListener {
                    onItemClickListener.invoke(category)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem.id.hashCode() == newItem.id.hashCode()
    }
}