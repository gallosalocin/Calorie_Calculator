package com.gallosalocin.caloriecalculator.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "dishes")
@Parcelize
@Keep
data class Dish(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "dishes_id")
    val id: Int = 0,
    val name: String,
) : Parcelable