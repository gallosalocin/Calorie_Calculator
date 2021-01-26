package com.gallosalocin.caloriecalculator.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "users")
@Parcelize
data class User(
        @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
        var gender: Int,
        var age: Int,
        var height: Int,
        var weight: Int,
        var activity: Int,
        var fatPercent: Int = 0,
        var carbPercent: Int = 0,
        var protPercent: Int = 0,
        var dailyCalories: Float = 0F,
        var fatResult: Int = 0,
        var carbResult: Int = 0,
        var protResult: Int = 0,
        var bmrResult: Float = 0F,
        var isCustomCalories: Boolean
) : Parcelable