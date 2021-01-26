package com.gallosalocin.caloriecalculator.ui.addFood

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import com.gallosalocin.caloriecalculator.models.User
import com.gallosalocin.caloriecalculator.repositories.MainRepository
import kotlinx.coroutines.launch

class AddFoodViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    val getAllCategories: LiveData<List<Category>> = mainRepository.observeAllCategories()

    fun insertFood(food: Food) = viewModelScope.launch {
        mainRepository.insertFood(food)
    }
}