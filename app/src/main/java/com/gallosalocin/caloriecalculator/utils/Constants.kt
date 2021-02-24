package com.gallosalocin.caloriecalculator.utils

import com.gallosalocin.caloriecalculator.BuildConfig

object Constants {

    const val CALORIES_CALCULATOR_DATABASE_NAME = "calories_calculator_db"

    const val DATA_STORE_NAME = "dataStorePref"
    const val KEY_FIRST_TIME_TOGGLE = "KEY_FIRST_TIME_TOGGLE"

    /** Nutritionix */
    const val BASE_URL = "https://trackapi.nutritionix.com/"
    const val NUTRITIONIX_APPLICATION_ID = BuildConfig.NutritionixApplicationId
    const val NUTRITIONIX_API_KEY = BuildConfig.NutritionixApiKey
}