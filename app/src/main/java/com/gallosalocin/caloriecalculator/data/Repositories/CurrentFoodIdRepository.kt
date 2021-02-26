package com.gallosalocin.caloriecalculator.data.Repositories

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CurrentFoodIdRepository {

    private val currentFoodIdMutableLiveData = MutableLiveData<Int>()

    @MainThread
    fun setCurrentFoodId(id: Int) {
        currentFoodIdMutableLiveData.value = id
    }

    fun getCurrentFoodIdLiveData() : LiveData<Int> = currentFoodIdMutableLiveData
}