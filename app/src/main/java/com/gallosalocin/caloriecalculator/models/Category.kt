package com.gallosalocin.caloriecalculator.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "categories")
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categories_id")
    val id: Int = 0,
    val name: String,
    val color: Int
) : Parcelable {

    override fun toString(): String {
        return name
    }
}