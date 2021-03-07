package com.gallosalocin.caloriecalculator.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_NO_CHOICE
import com.gallosalocin.caloriecalculator.utils.Constants.MEAL_TAG_NO_CHOICE
import kotlinx.parcelize.Parcelize

@Entity(
        tableName = "foods",
        foreignKeys = [
                ForeignKey(
                        entity = Category::class,
                        parentColumns = ["categories_id"],
                        childColumns = ["category_id"],
                        onDelete = ForeignKey.CASCADE
                ),
                ForeignKey(
                        entity = Dish::class,
                        parentColumns = ["dishes_id"],
                        childColumns = ["dish_id"],
                        onDelete = ForeignKey.CASCADE
                )]
)
@Parcelize
@Keep
data class Food(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "foods_id")
        var id: Int = 0,
        @ColumnInfo(name = "day_id")
        var dayId: String = DAY_TAG_NO_CHOICE,
        @ColumnInfo(name = "meal_id")
        var mealId: String = MEAL_TAG_NO_CHOICE,
        val name: String,
        @ColumnInfo(name = "category_id")
        var categoryId: Int,
        @ColumnInfo(name = "dish_id")
        var dishId: Int? = null,
        val weight: Int = 100,
        val calories: Float,
        val fats: Float,
        val carbs: Float,
        val prots: Float,
        val note: String = ""
) : Parcelable