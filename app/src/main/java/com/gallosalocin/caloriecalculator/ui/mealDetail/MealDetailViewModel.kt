package com.gallosalocin.caloriecalculator.ui.mealDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import com.gallosalocin.caloriecalculator.models.User
import com.gallosalocin.caloriecalculator.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val getUser: LiveData<User> = mainRepository.observeUser()

    val getMealDetail: LiveData<List<FoodWithCategory>> = mainRepository.observeMealDetail()

    val getDayDetail: LiveData<List<FoodWithCategory>> = mainRepository.observeDayDetail()

    fun deleteAllMealDetail() = viewModelScope.launch {
        mainRepository.deleteAllMealDetail()
    }

    fun insertFood(food: Food) = viewModelScope.launch {
        mainRepository.insertFood(food)
    }

    fun updateFood(food: Food) = viewModelScope.launch {
        mainRepository.updateFood(food)
    }

    fun deleteFood(food: Food) = viewModelScope.launch {
        mainRepository.deleteFood(food)
    }
}