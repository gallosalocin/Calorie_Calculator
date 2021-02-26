package com.gallosalocin.caloriecalculator.data.Repositories

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CurrentCategoryIdRepository {

    private val currentCategoryIdMutableLiveData = MutableLiveData<Int>()

    @MainThread
    fun setCurrentCategoryId(id: Int) {
        currentCategoryIdMutableLiveData.value = id
    }

    fun getCurrentCategoryIdLiveData() : LiveData<Int> = currentCategoryIdMutableLiveData
}