package com.gallosalocin.caloriecalculator.data.network.dto

import com.google.gson.annotations.SerializedName

data class FoodNutrients(
    @SerializedName("food_name")
    val name: String,
    @SerializedName("serving_weight_grams")
    val weight: Double,
    @SerializedName("nf_calories")
    val calories: Double,
    @SerializedName("nf_total_fat")
    val fats: Double,
    @SerializedName("nf_total_carbohydrate")
    val carbs: Double,
    @SerializedName("nf_protein")
    val prots: Double,
    @SerializedName("photo")
    val photo: FoodNutrientsPhoto,
)
