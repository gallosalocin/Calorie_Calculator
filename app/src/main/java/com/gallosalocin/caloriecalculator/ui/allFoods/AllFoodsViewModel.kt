package com.gallosalocin.caloriecalculator.ui.allFoods

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import com.gallosalocin.caloriecalculator.models.User
import com.gallosalocin.caloriecalculator.repositories.CurrentFoodIdRepository
import com.gallosalocin.caloriecalculator.repositories.MainRepository
import kotlinx.coroutines.launch

class AllFoodsViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val currentFoodIdRepository: CurrentFoodIdRepository
): ViewModel() {

        val getAllFoods: LiveData<List<FoodWithCategory>> = mainRepository.observeAllFoods()

        fun insertFood(food: Food) = viewModelScope.launch {
                mainRepository.insertFood(food)
        }

        fun updateFood(food: Food) = viewModelScope.launch {
                mainRepository.updateFood(food)
        }

        fun setCurrentFoodId(id: Int) {
                currentFoodIdRepository.setCurrentFoodId(id)
        }

}