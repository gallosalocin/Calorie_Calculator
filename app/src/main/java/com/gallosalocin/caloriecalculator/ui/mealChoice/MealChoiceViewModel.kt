package com.gallosalocin.caloriecalculator.ui.mealChoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gallosalocin.caloriecalculator.data.Repositories.Repository
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import com.gallosalocin.caloriecalculator.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MealChoiceViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    val getUser: LiveData<User> = repository.local.observeUser()

    val getDayDetail: LiveData<List<FoodWithCategory>> = repository.local.observeDayDetail()

    val getBreakfastMacros: LiveData<List<FoodWithCategory>> = repository.local.observeBreakfast()

    val getLunchMacros: LiveData<List<FoodWithCategory>> = repository.local.observeLunch()

    val getDinnerMacros: LiveData<List<FoodWithCategory>> = repository.local.observeDinner()

    val getSnackMacros: LiveData<List<FoodWithCategory>> = repository.local.observeSnack()
}