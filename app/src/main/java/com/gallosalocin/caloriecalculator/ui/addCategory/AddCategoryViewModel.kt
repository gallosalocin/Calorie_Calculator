package com.gallosalocin.caloriecalculator.ui.addCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.data.repositories.Repository
import com.gallosalocin.caloriecalculator.models.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun insertCategory(category: Category) = viewModelScope.launch {
        repository.local.insertCategory(category)
    }
}