package com.gallosalocin.caloriecalculator.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithAllData
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_NO_CHOICE


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
    @Query("SELECT * FROM foods WHERE day_id LIKE '$DAY_TAG_NO_CHOICE' ORDER BY lower(name)")
    fun getAllFoods(): LiveData<List<FoodWithAllData>>

    @Transaction
    @Query("SELECT * FROM foods WHERE day_id LIKE :dayId AND meal_id LIKE :mealId ORDER BY category_id, lower(name)")
    fun getMealDetail(dayId: String, mealId: String): LiveData<List<FoodWithAllData>>

    @Transaction
    @Query("SELECT * FROM foods WHERE day_id LIKE :dayId ORDER BY category_id, lower(name)")
    fun getDayDetail(dayId: String): LiveData<List<FoodWithAllData>>

    @Transaction
    @Query("SELECT * FROM foods WHERE dish_id LIKE :dishId ORDER BY category_id, lower(name)")
    fun getRecipeFoods(dishId: Int): LiveData<List<FoodWithAllData>>

    @Query("SELECT * FROM foods WHERE foods_id LIKE :foodId")
    fun getFoodWithId(foodId: Int): LiveData<FoodWithAllData>

}