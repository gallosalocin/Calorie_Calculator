package com.gallosalocin.caloriecalculator.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithCategory


@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFood(food: Food)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Transaction
    @Query("DELETE FROM foods WHERE day_id LIKE :dayId AND meal_id LIKE :mealId")
    suspend fun deleteAllMealDetail(dayId: String, mealId: String)

    @Transaction
    @Query("SELECT * FROM foods WHERE day_id LIKE '0' ORDER BY lower(name)")
    fun getAllFoods(): LiveData<List<FoodWithCategory>>

    @Transaction
    @Query("SELECT * FROM foods WHERE category_id LIKE '2' ORDER BY lower(name)")
    fun getAllDishes(): LiveData<List<FoodWithCategory>>

    @Transaction
    @Query("SELECT * FROM foods WHERE day_id LIKE :dayId AND meal_id LIKE :mealId ORDER BY category_id, lower(name)")
    fun getMealDetail(dayId: String, mealId: String): LiveData<List<FoodWithCategory>>

    @Transaction
    @Query("SELECT * FROM foods WHERE day_id LIKE :dayId ORDER BY category_id, lower(name)")
    fun getDayDetail(dayId: String): LiveData<List<FoodWithCategory>>

    @Query("SELECT * FROM foods WHERE foods_id LIKE :foodId")
    fun getFoodWithId(foodId: Int): LiveData<FoodWithCategory>

}