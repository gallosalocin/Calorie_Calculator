package com.gallosalocin.caloriecalculator.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "foods",
        foreignKeys = [ForeignKey(entity = Category::class,
                parentColumns = ["categories_id"],
                childColumns = ["category_id"],
                onDelete = ForeignKey.CASCADE)])
@Parcelize
data class Food(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "foods_id")
        var id: Int = 0,
        @ColumnInfo(name = "day_id")
        var dayId: String = "0",
        @ColumnInfo(name = "meal_id")
        var mealId: String = "0",
        val name: String,
        @ColumnInfo(name = "category_id")
        val categoryId: Int,
        val weight: Int = 100,
        val calories: Float,
        val fats: Float,
        val carbs: Float,
        val prots: Float,
        val note: String = ""
) : Parcelable