package com.gallosalocin.caloriecalculator.ui.foodDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.data.Repositories.CurrentFoodIdRepository
import com.gallosalocin.caloriecalculator.data.Repositories.Repository
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val repository: Repository,
    private val currentFoodIdRepository: CurrentFoodIdRepository
) : ViewModel() {

    fun getViewStateLiveData(): LiveData<FoodWithCategory> =
        Transformations.switchMap(currentFoodIdRepository.getCurrentFoodIdLiveData()) { id ->
            repository.local.observeFoodWithId(id)
        }

    fun deleteFood(food: Food) = viewModelScope.launch {
        repository.local.deleteFood(food)
    }

    fun setCurrentFoodId(id: Int) {
        currentFoodIdRepository.setCurrentFoodId(id)
    }
}