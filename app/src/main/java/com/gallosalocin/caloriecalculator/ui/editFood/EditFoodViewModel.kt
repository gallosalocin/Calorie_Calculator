package com.gallosalocin.caloriecalculator.ui.editFood

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import com.gallosalocin.caloriecalculator.repositories.CurrentFoodIdRepository
import com.gallosalocin.caloriecalculator.repositories.MainRepository
import kotlinx.coroutines.launch

class EditFoodViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val currentFoodIdRepository: CurrentFoodIdRepository
): ViewModel() {

    fun getViewStateLiveData() : LiveData<FoodWithCategory> {
        return Transformations.switchMap(currentFoodIdRepository.getCurrentFoodIdLiveData()) { id ->
            mainRepository.observeFoodWithId(id)
        }
    }

    fun updateFood(food: Food) = viewModelScope.launch {
        mainRepository.updateFood(food)
    }

    val getAllCategories: LiveData<List<Category>> = mainRepository.observeAllCategories()


}