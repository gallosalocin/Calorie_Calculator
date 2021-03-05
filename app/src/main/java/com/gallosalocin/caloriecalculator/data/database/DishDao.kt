package com.gallosalocin.caloriecalculator.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gallosalocin.caloriecalculator.models.Dish

@Dao
interface DishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: Dish)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDish(dish: Dish)

    @Delete
    suspend fun deleteDish(dish: Dish)

    @Query("SELECT * FROM dishes ORDER BY lower(name)")
    fun getAllDishes(): LiveData<List<Dish>>

    @Query("SELECT * FROM dishes WHERE dishes_id LIKE :dishId")
    fun getDishWithId(dishId: Int): LiveData<Dish>

    @Query("SELECT * FROM dishes WHERE name LIKE ''")
    fun getNewDishId(): LiveData<Dish>

}