package com.gallosalocin.caloriecalculator.data.repositories

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CurrentDishIdRepository {

    private val currentDishIdMutableLiveData = MutableLiveData<Int>()

    @MainThread
    fun setCurrentDishId(id: Int) {
        currentDishIdMutableLiveData.value = id
    }

    fun getCurrentDishIdLiveData(): LiveData<Int> = currentDishIdMutableLiveData
}