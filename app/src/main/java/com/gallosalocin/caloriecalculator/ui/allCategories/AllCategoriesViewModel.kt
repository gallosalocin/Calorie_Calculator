package com.gallosalocin.caloriecalculator.ui.allCategories

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.repositories.CurrentCategoryIdRepository
import com.gallosalocin.caloriecalculator.repositories.MainRepository

class AllCategoriesViewModel @ViewModelInject constructor(
    mainRepository: MainRepository,
    private val currentCategoryIdRepository: CurrentCategoryIdRepository

) : ViewModel() {

    val getAllCategories: LiveData<List<Category>> = mainRepository.observeAllCategories()

    fun setCurrentCategoryId(id: Int) {
        currentCategoryIdRepository.setCurrentCategoryId(id)
    }
}