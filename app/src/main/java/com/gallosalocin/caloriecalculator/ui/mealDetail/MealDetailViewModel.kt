package com.gallosalocin.caloriecalculator.ui.mealDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.data.Repositories.Repository
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import com.gallosalocin.caloriecalculator.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val getUser: LiveData<User> = repository.local.observeUser()

    val getMealDetail: LiveData<List<FoodWithCategory>> = repository.local.observeMealDetail()

    val getDayDetail: LiveData<List<FoodWithCategory>> = repository.local.observeDayDetail()

    fun deleteAllMealDetail() = viewModelScope.launch {
        repository.local.deleteAllMealDetail()
    }

    fun insertFood(food: Food) = viewModelScope.launch {
        repository.local.insertFood(food)
    }

    fun updateFood(food: Food) = viewModelScope.launch {
        repository.local.updateFood(food)
    }

    fun deleteFood(food: Food) = viewModelScope.launch {
        repository.local.deleteFood(food)
    }
}