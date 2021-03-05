package com.gallosalocin.caloriecalculator.data.database

import androidx.room.TypeConverter
import com.gallosalocin.caloriecalculator.models.FoodWithAllData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FoodTypeConverter {

    @TypeConverter
    fun foodWithAllDataArrayListToString(foodList: List<FoodWithAllData>?): String? {
        val type = object : TypeToken<List<FoodWithAllData>>() {}.type
        return Gson().toJson(foodList, type)
    }

    @TypeConverter
    fun stringToFoodWithAllDataArrayList(data: String?): List<FoodWithAllData>? {
        val type = object : TypeToken<List<FoodWithAllData>>() {}.type
        return Gson().fromJson(data, type)
    }
}

