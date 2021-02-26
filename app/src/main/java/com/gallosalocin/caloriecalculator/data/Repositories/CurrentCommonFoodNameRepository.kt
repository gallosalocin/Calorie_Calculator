package com.gallosalocin.caloriecalculator.data.Repositories

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CurrentCommonFoodNameRepository {

    private val currentCommonFoodNameMutableLiveData = MutableLiveData<String>()

    @MainThread
    fun setCurrentCommonFoodName(commonFoodName: String) {
        currentCommonFoodNameMutableLiveData.value = commonFoodName
    }

    fun getCurrentCommonFoodNameLiveData(): LiveData<String> = currentCommonFoodNameMutableLiveData
}