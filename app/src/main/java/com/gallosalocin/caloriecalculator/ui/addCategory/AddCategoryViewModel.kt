package com.gallosalocin.caloriecalculator.ui.addCategory

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.repositories.MainRepository
import kotlinx.coroutines.launch

class AddCategoryViewModel @ViewModelInject constructor(
        private val mainRepository: MainRepository
) : ViewModel() {

    fun insertCategory(category: Category) = viewModelScope.launch {
        mainRepository.insertCategory(category)
    }
}