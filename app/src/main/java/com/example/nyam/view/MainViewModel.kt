package com.example.nyam.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.nyam.data.NyamRepository
import com.example.nyam.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: NyamRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun login(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}