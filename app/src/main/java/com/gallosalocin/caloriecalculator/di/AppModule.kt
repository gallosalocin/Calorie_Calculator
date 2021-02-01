package com.gallosalocin.caloriecalculator.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.gallosalocin.caloriecalculator.db.CaloriesCalculatorDatabase
import com.gallosalocin.caloriecalculator.others.Constants.CALORIES_CALCULATOR_DATABASE_NAME
import com.gallosalocin.caloriecalculator.others.Constants.KEY_FIRST_TIME_TOGGLE
import com.gallosalocin.caloriecalculator.others.Constants.SHARED_PREFERENCES_NAME
import com.gallosalocin.caloriecalculator.repositories.CurrentCategoryIdRepository
import com.gallosalocin.caloriecalculator.repositories.CurrentFoodIdRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrentFoodRepository() = CurrentFoodIdRepository()

    @Singleton
    @Provides
    fun provideCurrentCategoryRepository() = CurrentCategoryIdRepository()

    @Singleton
    @Provides
    fun provideRealEstateDatabase(@ApplicationContext context: Context, callback: CaloriesCalculatorDatabase.Callback) =
        Room.databaseBuilder(context, CaloriesCalculatorDatabase::class.java, CALORIES_CALCULATOR_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Singleton
    @Provides
    fun provideFoodDao(database: CaloriesCalculatorDatabase) = database.getFoodDao()

    @Singleton
    @Provides
    fun provideCategoryDao(database: CaloriesCalculatorDatabase) = database.getCategoryDao()

    @Singleton
    @Provides
    fun provideUserDao(database: CaloriesCalculatorDatabase) = database.getUserDao()

    @Singleton
    @Provides
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPref: SharedPreferences) = sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)
}