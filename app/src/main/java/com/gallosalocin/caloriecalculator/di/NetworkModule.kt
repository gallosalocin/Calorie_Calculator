package com.gallosalocin.caloriecalculator.di

import com.gallosalocin.caloriecalculator.data.network.FoodNutrientsApi
import com.gallosalocin.caloriecalculator.data.network.InstantSearchApi
import com.gallosalocin.caloriecalculator.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Singleton
    @Provides
    fun provideInstantSearchApi(retrofit: Retrofit): InstantSearchApi =
        retrofit.create(InstantSearchApi::class.java)

    @Singleton
    @Provides
    fun provideFoodNutrientsApi(retrofit: Retrofit): FoodNutrientsApi =
        retrofit.create(FoodNutrientsApi::class.java)

}