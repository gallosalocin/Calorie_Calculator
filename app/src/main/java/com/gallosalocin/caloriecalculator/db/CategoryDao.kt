package com.gallosalocin.caloriecalculator.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.models.FoodWithCategory

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories ORDER BY name")
    fun getAllCategories(): LiveData<List<Category>>

}