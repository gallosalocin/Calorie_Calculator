package com.gallosalocin.caloriecalculator.data.network

import com.gallosalocin.caloriecalculator.data.network.dto.InstantSearchResponse
import com.gallosalocin.caloriecalculator.utils.Constants.NUTRITIONIX_API_KEY
import com.gallosalocin.caloriecalculator.utils.Constants.NUTRITIONIX_APPLICATION_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface InstantSearchApi {

    @Headers(
        "x-app-id: $NUTRITIONIX_APPLICATION_ID",
        "x-app-key: $NUTRITIONIX_API_KEY"
    )
    @GET("v2/search/instant")
    suspend fun getInstantSearch(
        @Query("query") query: String
    ): Response<InstantSearchResponse>
}