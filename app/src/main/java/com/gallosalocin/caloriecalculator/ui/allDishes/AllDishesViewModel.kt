package com.gallosalocin.caloriecalculator.ui.allDishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.data.repositories.CurrentDishIdRepository
import com.gallosalocin.caloriecalculator.data.repositories.Repository
import com.gallosalocin.caloriecalculator.models.Dish
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithAllData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllDishesViewModel @Inject constructor(
    private val repository: Repository,
    private val currentDishIdRepository: CurrentDishIdRepository

) : ViewModel() {

    val getAllDishes: LiveData<List<Dish>> = repository.local.observeAllDishes()

    fun insertFood(food: Food) = viewModelScope.launch {
        repository.local.insertFood(food)
    }

    fun setCurrentDishId(id: Int) {
        currentDishIdRepository.setCurrentDishId(id)
    }

    fun insertDish(dish: Dish) = viewModelScope.launch {
        repository.local.insertDish(dish)
    }

    fun updateDish(dish: Dish) = viewModelScope.launch {
        repository.local.updateDish(dish)
    }

    fun deleteDish(dish: Dish) = viewModelScope.launch {
        repository.local.deleteDish(dish)
    }

    fun getDishFoodListLiveData(): LiveData<List<FoodWithAllData>> =
        Transformations.switchMap(currentDishIdRepository.getCurrentDishIdLiveData()) { dishId ->
            repository.local.observeRecipeFoods(dishId)
        }
}