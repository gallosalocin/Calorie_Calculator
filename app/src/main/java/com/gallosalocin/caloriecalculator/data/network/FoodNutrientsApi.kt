package com.gallosalocin.caloriecalculator.data.network

import com.gallosalocin.caloriecalculator.data.network.dto.FoodNamePost
import com.gallosalocin.caloriecalculator.data.network.dto.FoodNutrientsResponse
import com.gallosalocin.caloriecalculator.utils.Constants.NUTRITIONIX_API_KEY
import com.gallosalocin.caloriecalculator.utils.Constants.NUTRITIONIX_APPLICATION_ID
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FoodNutrientsApi {

    @Headers(
        "Content-Type: application/json",
        "x-app-id: $NUTRITIONIX_APPLICATION_ID",
        "x-app-key: $NUTRITIONIX_API_KEY",
        "x-remote-user-id: 0"
    )
    @POST("v2/natural/nutrients")
    suspend fun pushFoodNutrients(
        @Body foodNamePost: FoodNamePost
    ): Response<FoodNutrientsResponse>
}