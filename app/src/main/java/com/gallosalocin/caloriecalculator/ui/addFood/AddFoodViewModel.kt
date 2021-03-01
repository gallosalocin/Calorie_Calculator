package com.gallosalocin.caloriecalculator.ui.addFood

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.data.repositories.Repository
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.models.Food
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val getAllCategories: LiveData<List<Category>> = repository.local.observeAllCategories()

    fun insertFood(food: Food) = viewModelScope.launch {
        repository.local.insertFood(food)
    }
}