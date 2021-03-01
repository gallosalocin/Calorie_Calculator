package com.gallosalocin.caloriecalculator.ui.allCategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gallosalocin.caloriecalculator.data.repositories.CurrentCategoryIdRepository
import com.gallosalocin.caloriecalculator.data.repositories.Repository
import com.gallosalocin.caloriecalculator.models.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllCategoriesViewModel @Inject constructor(
    repository: Repository,
    private val currentCategoryIdRepository: CurrentCategoryIdRepository

) : ViewModel() {

    val getAllCategories: LiveData<List<Category>> = repository.local.observeAllCategories()

    fun setCurrentCategoryId(id: Int) {
        currentCategoryIdRepository.setCurrentCategoryId(id)
    }
}