package com.gallosalocin.caloriecalculator.utils

import com.gallosalocin.caloriecalculator.BuildConfig

object Constants {

    /** generalChoices */
    const val GLOBAL_CHOICE_NO_CHOICE = "GLOBAL_CHOICE_NO_CHOICE"
    const val GLOBAL_CHOICE_BOTTOM = "GLOBAL_CHOICE_BOTTOM"
    const val GLOBAL_CHOICE_BOTTOM_DISHES = "GLOBAL_CHOICE_BOTTOM_DISHES"
    const val GLOBAL_CHOICE_BOTTOM_PROFILE = "GLOBAL_CHOICE_BOTTOM_PROFILE"
    const val GLOBAL_CHOICE_BOTTOM_FOODS = "GLOBAL_CHOICE_BOTTOM_FOODS"

    /** dayTag */
    const val DAY_TAG_NO_CHOICE = "No Choice"
    const val DAY_TAG_MONDAY = "Monday"
    const val DAY_TAG_TUESDAY = "Tuesday"
    const val DAY_TAG_WEDNESDAY = "Wednesday"
    const val DAY_TAG_THURSDAY = "Thursday"
    const val DAY_TAG_FRIDAY = "Friday"
    const val DAY_TAG_SATURDAY = "Saturday"
    const val DAY_TAG_SUNDAY = "Sunday"

    /** mealTag */
    const val MEAL_TAG_NO_CHOICE = "No Choice"
    const val MEAL_TAG_BREAKFAST = "Breakfast"
    const val MEAL_TAG_LUNCH = "Lunch"
    const val MEAL_TAG_DINNER = "Dinner"
    const val MEAL_TAG_SNACK = "Snack"

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