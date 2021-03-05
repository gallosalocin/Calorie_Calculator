package com.gallosalocin.caloriecalculator.utils

import com.gallosalocin.caloriecalculator.BuildConfig

object Constants {

    /** generalChoices */
    const val GLOBAL_CHOICE_NOTHING = "GLOBAL_CHOICE_NOTHING"
    const val GLOBAL_CHOICE_BOTTOM = "GLOBAL_CHOICE_BOTTOM"
    const val GLOBAL_CHOICE_BOTTOM_DISHES = "GLOBAL_CHOICE_BOTTOM_DISHES"
    const val GLOBAL_CHOICE_BOTTOM_PROFILE = "GLOBAL_CHOICE_BOTTOM_PROFILE"
    const val GLOBAL_CHOICE_BOTTOM_FOODS = "GLOBAL_CHOICE_BOTTOM_FOODS"

    /** Database */
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