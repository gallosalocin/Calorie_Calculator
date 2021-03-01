package com.gallosalocin.caloriecalculator.utils

import com.gallosalocin.caloriecalculator.BuildConfig

object Constants {

    const val CALORIES_CALCULATOR_DATABASE_NAME = "calories_calculator_db"

    /** Preferences */
    const val DATA_STORE_NAME = "dataStorePref"
    const val PREFERENCES_FIRST_TIME_TOGGLE = "firstTimeToggle"
    const val PREFERENCES_BACK_ONLINE = "backOnline"

    /** Nutritionix */
    const val BASE_URL = "https://trackapi.nutritionix.com/"

    const val NUTRITIONIX_APPLICATION_ID = BuildConfig.NutritionixApplicationId  // Instead of BuildConfig.NutritionixApplicationId put "YOUR_APPLICATION_ID"
    const val NUTRITIONIX_API_KEY = BuildConfig.NutritionixApiKey                // Instead of BuildConfig.NutritionixApiKey put "YOUR_API_KEY"
}