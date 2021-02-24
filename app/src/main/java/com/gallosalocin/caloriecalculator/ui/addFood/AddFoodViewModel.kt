package com.gallosalocin.caloriecalculator.ui.addFood

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val getAllCategories: LiveData<List<Category>> = mainRepository.observeAllCategories()

    fun insertFood(food: Food) = viewModelScope.launch {
        mainRepository.insertFood(food)
    }
}