package com.gallosalocin.caloriecalculator.ui.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallosalocin.caloriecalculator.models.User
import com.gallosalocin.caloriecalculator.repositories.MainRepository
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    val getUser: LiveData<User> = mainRepository.observeUser()

    fun insertUser(user: User) = viewModelScope.launch {
        mainRepository.insertUser(user)
    }
}