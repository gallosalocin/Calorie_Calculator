package com.gallosalocin.caloriecalculator.ui.foodDetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import com.gallosalocin.caloriecalculator.repositories.CurrentFoodIdRepository
import com.gallosalocin.caloriecalculator.repositories.MainRepository
import kotlinx.coroutines.launch

class FoodDetailViewModel @ViewModelInject constructor(
        private val mainRepository: MainRepository,
        private val currentFoodIdRepository: CurrentFoodIdRepository
) : ViewModel() {

    fun getViewStateLiveData(): LiveData<FoodWithCategory> =
        Transformations.switchMap(currentFoodIdRepository.getCurrentFoodIdLiveData()) { id ->
            mainRepository.observeFoodWithId(id)
        }

    fun deleteFood(food: Food) = viewModelScope.launch {
        mainRepository.deleteFood(food)
    }

    fun setCurrentFoodId(id: Int) {
        currentFoodIdRepository.setCurrentFoodId(id)
    }
}