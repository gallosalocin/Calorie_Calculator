package com.gallosalocin.caloriecalculator.di

import android.content.Context
import androidx.room.Room
import com.gallosalocin.caloriecalculator.data.database.CaloriesCalculatorDatabase
import com.gallosalocin.caloriecalculator.utils.Constants.CALORIES_CALCULATOR_DATABASE_NAME
import com.gallosalocin.caloriecalculator.data.repositories.CurrentCategoryIdRepository
import com.gallosalocin.caloriecalculator.data.repositories.CurrentCommonFoodNameRepository
import com.gallosalocin.caloriecalculator.data.repositories.CurrentFoodIdRepository
import com.gallosalocin.caloriecalculator.data.repositories.DataStoreRepository
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
    fun provideCurrentCommonFoodNameRepository() = CurrentCommonFoodNameRepository()

    @Singleton
    @Provides
    fun provideDataStoreRepository(@ApplicationContext context: Context) = DataStoreRepository(context)

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

}