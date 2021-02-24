package com.gallosalocin.caloriecalculator.ui.addCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun insertCategory(category: Category) = viewModelScope.launch {
        mainRepository.insertCategory(category)
    }
}