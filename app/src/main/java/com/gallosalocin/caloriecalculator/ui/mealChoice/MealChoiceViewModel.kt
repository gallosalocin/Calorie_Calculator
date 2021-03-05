package com.gallosalocin.caloriecalculator.ui.mealChoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gallosalocin.caloriecalculator.data.repositories.Repository
import com.gallosalocin.caloriecalculator.models.FoodWithAllData
import com.gallosalocin.caloriecalculator.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MealChoiceViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    val getUser: LiveData<User> = repository.local.observeUser()

    val getDayDetail: LiveData<List<FoodWithAllData>> = repository.local.observeDayDetail()

    val getBreakfastMacros: LiveData<List<FoodWithAllData>> = repository.local.observeBreakfast()

    val getLunchMacros: LiveData<List<FoodWithAllData>> = repository.local.observeLunch()

    val getDinnerMacros: LiveData<List<FoodWithAllData>> = repository.local.observeDinner()

    val getSnackMacros: LiveData<List<FoodWithAllData>> = repository.local.observeSnack()
}