package com.gallosalocin.caloriecalculator.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.models.User
import com.gallosalocin.caloriecalculator.repositories.DataStoreRepository
import com.gallosalocin.caloriecalculator.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val getUser: LiveData<User> = mainRepository.observeUser()

    fun insertUser(user: User) = viewModelScope.launch {
        mainRepository.insertUser(user)
    }

    val readFromDataStore = dataStoreRepository.readFromDataStore.asLiveData()

    fun saveToDataStore(isFirstTimeOpen: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveToDataStore(isFirstTimeOpen)
    }
}