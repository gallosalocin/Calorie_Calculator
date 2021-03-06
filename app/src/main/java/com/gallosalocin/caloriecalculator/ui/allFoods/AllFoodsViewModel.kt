package com.gallosalocin.caloriecalculator.ui.allFoods

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.data.repositories.CurrentDishIdRepository
import com.gallosalocin.caloriecalculator.data.repositories.CurrentFoodIdRepository
import com.gallosalocin.caloriecalculator.data.repositories.Repository
import com.gallosalocin.caloriecalculator.models.Dish
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithAllData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllFoodsViewModel @Inject constructor(
    private val repository: Repository,
    private val currentFoodIdRepository: CurrentFoodIdRepository,
    private val currentDishIdRepository: CurrentDishIdRepository,
) : ViewModel() {

    val getAllFoods: LiveData<List<FoodWithAllData>> = repository.local.observeAllFoods()

    fun insertFood(food: Food) = viewModelScope.launch {
        repository.local.insertFood(food)
    }

    fun updateFood(food: Food) = viewModelScope.launch {
        repository.local.updateFood(food)
    }

    fun setCurrentFoodId(id: Int) {
        currentFoodIdRepository.setCurrentFoodId(id)
    }

    fun getViewStateLiveData(): LiveData<Dish> =
        Transformations.switchMap(currentDishIdRepository.getCurrentDishIdLiveData()) { id ->
            repository.local.observeDishWithId(id)
        }

}