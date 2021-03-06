package com.gallosalocin.caloriecalculator.utils

import com.gallosalocin.caloriecalculator.BuildConfig

object Constants {

    /** generalChoices */
    const val GLOBAL_CHOICE_NOTHING = "GLOBAL_CHOICE_NOTHING"
    const val GLOBAL_CHOICE_BOTTOM = "GLOBAL_CHOICE_BOTTOM"
    const val GLOBAL_CHOICE_BOTTOM_DISHES = "GLOBAL_CHOICE_BOTTOM_DISHES"
    const val GLOBAL_CHOICE_BOTTOM_PROFILE = "GLOBAL_CHOICE_BOTTOM_PROFILE"
    const val GLOBAL_CHOICE_BOTTOM_FOODS = "GLOBAL_CHOICE_BOTTOM_FOODS"

    /** dayTag */
    const val DAY_TAG_NOTHING = "DAY_TAG_NOTHING"
    const val DAY_TAG_MONDAY = "DAY_TAG_MONDAY"
    const val DAY_TAG_TUESDAY = "DAY_TAG_TUESDAY"
    const val DAY_TAG_WEDNESDAY = "DAY_TAG_WEDNESDAY"
    const val DAY_TAG_THURSDAY = "DAY_TAG_THURSDAY"
    const val DAY_TAG_FRIDAY = "DAY_TAG_FRIDAY"
    const val DAY_TAG_SATURDAY = "DAY_TAG_SATURDAY"
    const val DAY_TAG_SUNDAY = "DAY_TAG_SUNDAY"

    /** mealTag */
    const val MEAL_TAG_NOTHING = "MEAL_TAG_NOTHING"
    const val MEAL_TAG_BREAKFAST = "MEAL_TAG_BREAKFAST"
    const val MEAL_TAG_LUNCH = "MEAL_TAG_LUNCH"
    const val MEAL_TAG_DINNER = "MEAL_TAG_DINNER"
    const val MEAL_TAG_SNACK = "MEAL_TAG_SNACK"

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