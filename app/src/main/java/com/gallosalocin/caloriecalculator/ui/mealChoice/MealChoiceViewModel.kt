package com.gallosalocin.caloriecalculator.ui.mealChoice

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import com.gallosalocin.caloriecalculator.models.User
import com.gallosalocin.caloriecalculator.repositories.MainRepository

class MealChoiceViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    val getUser: LiveData<User> = mainRepository.observeUser()

    val getDayDetail: LiveData<List<FoodWithCategory>> = mainRepository.observeDayDetail()

    val getBreakfastMacros: LiveData<List<FoodWithCategory>> = mainRepository.observeBreakfast()

    val getLunchMacros: LiveData<List<FoodWithCategory>> = mainRepository.observeLunch()

    val getDinnerMacros: LiveData<List<FoodWithCategory>> = mainRepository.observeDinner()

    val getSnackMacros: LiveData<List<FoodWithCategory>> = mainRepository.observeSnack()
}