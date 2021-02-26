package com.gallosalocin.caloriecalculator.ui.nutritionixSearch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.data.Repositories.Repository
import com.gallosalocin.caloriecalculator.data.network.dto.InstantSearchResponse
import com.gallosalocin.caloriecalculator.data.Repositories.CurrentCommonFoodNameRepository
import com.gallosalocin.caloriecalculator.utils.NetworkResult
import com.gallosalocin.caloriecalculator.utils.Utils.Companion.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NutritionixSearchViewModel @Inject constructor(
    private val repository: Repository,
    private val currentCommonFoodNameRepository: CurrentCommonFoodNameRepository,
    application: Application,
) : AndroidViewModel(application) {

    var instantSearchResponse: MutableLiveData<NetworkResult<InstantSearchResponse>> = MutableLiveData()


    fun setCurrentCommonFoodName(commonFoodName: String) {
        currentCommonFoodNameRepository.setCurrentCommonFoodName(commonFoodName)
    }

    fun getInstantSearch(query: String) = viewModelScope.launch {
        getInstantSearchSafeCall(query)
    }

    private suspend fun getInstantSearchSafeCall(query: String) {
        instantSearchResponse.value = NetworkResult.Loading()
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.getInstantSearch(query)
                instantSearchResponse.value = handleCommonFoodsResponse(response)
            } catch (e: Exception) {
                instantSearchResponse.value = NetworkResult.Error("Foods not found.")
            }
        } else {
            instantSearchResponse.value = NetworkResult.Error("No internet connection.")
        }
    }

    private fun handleCommonFoodsResponse(response: Response<InstantSearchResponse>): NetworkResult<InstantSearchResponse> {
        when {
            response.code() == 400 -> {
                return NetworkResult.Error("Validation Error, Invalid input parameters, Invalid request")
            }
            response.code() == 401 -> {
                return NetworkResult.Error("Unauthorized, Invalid auth keys, Usage limits exceeded, Missing tokens")
            }
            response.code() == 403 -> {
                return NetworkResult.Error("Forbidden, Disallowed entity")
            }
            response.code() == 404 -> {
                return NetworkResult.Error("Resource not found")
            }
            response.code() == 409 -> {
                return NetworkResult.Error("Resource conflict, Resource already exists")
            }
            response.code() == 500 -> {
                return NetworkResult.Error("Base error, internal server error, request failed")
            }
            response.body()!!.common.isEmpty() -> {
                return NetworkResult.Error("Foods not found.")
            }
            response.isSuccessful -> {
                val instantSearch = response.body()
                return NetworkResult.Success(instantSearch!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

}