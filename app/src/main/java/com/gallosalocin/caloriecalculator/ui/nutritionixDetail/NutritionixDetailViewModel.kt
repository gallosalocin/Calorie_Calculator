package com.gallosalocin.caloriecalculator.ui.nutritionixDetail

import android.app.Application
import androidx.lifecycle.*
import com.gallosalocin.caloriecalculator.data.repositories.Repository
import com.gallosalocin.caloriecalculator.data.network.dto.FoodNamePost
import com.gallosalocin.caloriecalculator.data.network.dto.FoodNutrientsResponse
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.data.repositories.CurrentCommonFoodNameRepository
import com.gallosalocin.caloriecalculator.utils.NetworkResult
import com.gallosalocin.caloriecalculator.utils.Utils.Companion.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NutritionixDetailViewModel @Inject constructor(
    private val repository: Repository,
    private val currentCommonFoodNameRepository: CurrentCommonFoodNameRepository,
    application: Application,
) : AndroidViewModel(application) {

    val getAllCategories: LiveData<List<Category>> = repository.local.observeAllCategories()

    var foodNutrientsResponse: MutableLiveData<NetworkResult<FoodNutrientsResponse>> = MutableLiveData()

    fun insertFood(food: Food) = viewModelScope.launch {
        repository.local.insertFood(food)
    }

    fun getCurrentCommonFoodNameLiveData(): LiveData<String> = currentCommonFoodNameRepository.getCurrentCommonFoodNameLiveData()

    fun pushFoodNutrients(foodNamePost: FoodNamePost) = viewModelScope.launch {
        pushFoodNutrientsSafeCall(foodNamePost)
    }

    private suspend fun pushFoodNutrientsSafeCall(foodNamePost: FoodNamePost) {
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.pushFoodNutrients(foodNamePost)
                foodNutrientsResponse.value = handleCommonFoodsResponse(response)
            } catch (e: Exception) {
                foodNutrientsResponse.value = NetworkResult.Error("Nutrients not found.")
            }
        } else {
            foodNutrientsResponse.value = NetworkResult.Error("No internet connection.")
        }
    }

    private fun handleCommonFoodsResponse(response: Response<FoodNutrientsResponse>): NetworkResult<FoodNutrientsResponse> {
        return when {
            response.body()!!.foods.isNullOrEmpty() -> {
                return NetworkResult.Error("Nutrients not found.")
            }
            response.isSuccessful -> {
                val foodNutrients = response.body()
                NetworkResult.Success(foodNutrients!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }
}