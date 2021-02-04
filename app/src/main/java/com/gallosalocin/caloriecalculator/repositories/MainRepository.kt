package com.gallosalocin.caloriecalculator.repositories

import androidx.lifecycle.LiveData
import com.gallosalocin.caloriecalculator.db.CategoryDao
import com.gallosalocin.caloriecalculator.db.FoodDao
import com.gallosalocin.caloriecalculator.db.UserDao
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import com.gallosalocin.caloriecalculator.models.User
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.dayTag
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.mealTag
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val foodDao: FoodDao,
    private val categoryDao: CategoryDao,
    private val userDao: UserDao
) {

    // Food

    suspend fun insertFood(food: Food) = foodDao.insertFood(food)

    suspend fun updateFood(food: Food) = foodDao.updateFood(food)

    suspend fun deleteFood(food: Food) = foodDao.deleteFood(food)

    suspend fun deleteAllMealDetail() = foodDao.deleteAllMealDetail(dayTag.toString(), mealTag.toString())

    fun observeAllFoods() = foodDao.getAllFoods()

    fun observeMealDetail() = foodDao.getMealDetail(dayTag.toString(), mealTag.toString())

    fun observeBreakfast() = foodDao.getMealDetail(dayTag.toString(), "1")
    fun observeLunch() = foodDao.getMealDetail(dayTag.toString(), "2")
    fun observeDinner() = foodDao.getMealDetail(dayTag.toString(), "3")
    fun observeSnack() = foodDao.getMealDetail(dayTag.toString(), "4")

    fun observeDayDetail() = foodDao.getDayDetail(dayTag.toString())

    fun observeFoodWithId(foodId: Int) : LiveData<FoodWithCategory> = foodDao.getFoodWithId(foodId)


    // Category

    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)

    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)

    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)

    fun observeAllCategories() = categoryDao.getAllCategories()

    fun observeCategoryWithId(categoryId: Int) : LiveData<Category> = categoryDao.getCategoryWithId(categoryId)


    // User

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    fun observeUser() = userDao.getUser()

}