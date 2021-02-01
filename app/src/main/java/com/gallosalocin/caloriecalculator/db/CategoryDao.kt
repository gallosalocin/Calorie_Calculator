package com.gallosalocin.caloriecalculator.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gallosalocin.caloriecalculator.models.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories ORDER BY lower(name)")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE categories_id LIKE :categoryId")
    fun getCategoryWithId(categoryId: Int): LiveData<Category>
}