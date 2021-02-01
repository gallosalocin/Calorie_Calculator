package com.gallosalocin.caloriecalculator.ui.editCategory

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.repositories.CurrentCategoryIdRepository
import com.gallosalocin.caloriecalculator.repositories.MainRepository
import kotlinx.coroutines.launch

class EditCategoryViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val currentCategoryIdRepository: CurrentCategoryIdRepository
) : ViewModel() {

    fun getViewStateLiveData(): LiveData<Category> =
        Transformations.switchMap(currentCategoryIdRepository.getCurrentCategoryIdLiveData()) { id ->
            mainRepository.observeCategoryWithId(id)
        }

    val getAllCategories: LiveData<List<Category>> = mainRepository.observeAllCategories()

    fun updateCategory(category: Category) = viewModelScope.launch {
        mainRepository.updateCategory(category)
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        mainRepository.deleteCategory(category)
    }
}