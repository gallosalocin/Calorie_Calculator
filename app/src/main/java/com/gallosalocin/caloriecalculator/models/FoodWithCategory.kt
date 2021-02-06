package com.gallosalocin.caloriecalculator.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class FoodWithCategory(
        @Embedded
        val food: Food,
        @Relation(entity = Category::class, entityColumn = "categories_id", parentColumn = "category_id")
        val category: Category,
) : Parcelable