package com.gallosalocin.caloriecalculator.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class FoodWithAllData(
        @Embedded
        val food: Food,
        @Relation(entity = Category::class, entityColumn = "categories_id", parentColumn = "category_id")
        val category: Category,
        @Relation(entity = Dish::class, entityColumn = "dishes_id", parentColumn = "dish_id")
        val dish: Dish?,
) : Parcelable