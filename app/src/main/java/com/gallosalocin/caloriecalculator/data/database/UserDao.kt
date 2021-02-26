package com.gallosalocin.caloriecalculator.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gallosalocin.caloriecalculator.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE id LIKE 1")
    fun getUser(): LiveData<User>
}