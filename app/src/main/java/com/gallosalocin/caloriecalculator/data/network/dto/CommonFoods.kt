package com.gallosalocin.caloriecalculator.data.network.dto

import com.google.gson.annotations.SerializedName

data class CommonFoods(
    @SerializedName("food_name")
    val foodName: String,
    val photo: CommonFoodsPhoto,
)
