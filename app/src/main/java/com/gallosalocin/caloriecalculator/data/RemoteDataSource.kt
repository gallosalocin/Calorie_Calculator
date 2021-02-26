package com.gallosalocin.caloriecalculator.data

import com.gallosalocin.caloriecalculator.data.network.FoodNutrientsApi
import com.gallosalocin.caloriecalculator.data.network.InstantSearchApi
import com.gallosalocin.caloriecalculator.data.network.dto.FoodNamePost
import com.gallosalocin.caloriecalculator.data.network.dto.FoodNutrientsResponse
import com.gallosalocin.caloriecalculator.data.network.dto.InstantSearchResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val instantSearchApi: InstantSearchApi,
    private val foodNutrientsApi: FoodNutrientsApi
) {

    suspend fun getInstantSearch(query: String): Response<InstantSearchResponse> = instantSearchApi.getInstantSearch(query)

    suspend fun pushFoodNutrients(foodNamePost: FoodNamePost): Response<FoodNutrientsResponse> = foodNutrientsApi.pushFoodNutrients(foodNamePost)

}