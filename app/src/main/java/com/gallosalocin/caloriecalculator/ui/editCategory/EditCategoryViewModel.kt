package com.gallosalocin.caloriecalculator.ui.editCategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.data.repositories.CurrentCategoryIdRepository
import com.gallosalocin.caloriecalculator.data.repositories.Repository
import com.gallosalocin.caloriecalculator.models.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    private val repository: Repository,
    private val currentCategoryIdRepository: CurrentCategoryIdRepository
) : ViewModel() {

    fun getViewStateLiveData(): LiveData<Category> =
        Transformations.switchMap(currentCategoryIdRepository.getCurrentCategoryIdLiveData()) { id ->
            repository.local.observeCategoryWithId(id)
        }

    val getAllCategories: LiveData<List<Category>> = repository.local.observeAllCategories()

    fun updateCategory(category: Category) = viewModelScope.launch {
        repository.local.updateCategory(category)
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        repository.local.deleteCategory(category)
    }
}