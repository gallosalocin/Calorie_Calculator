package com.gallosalocin.caloriecalculator.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.data.repositories.DataStoreRepository
import com.gallosalocin.caloriecalculator.data.repositories.Repository
import com.gallosalocin.caloriecalculator.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val getUser: LiveData<User> = repository.local.observeUser()

    fun insertUser(user: User) = viewModelScope.launch {
        repository.local.insertUser(user)
    }

    val readFromDataStore = dataStoreRepository.readFirstTimeToggle.asLiveData()

    fun saveToDataStore(isFirstTimeOpen: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveFirstTimeToggle(isFirstTimeOpen)
    }
}