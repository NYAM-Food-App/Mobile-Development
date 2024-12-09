package com.example.nyam.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nyam.data.NyamRepository
import com.example.nyam.data.ResultState
import com.example.nyam.data.remote.response.PostResponse
import com.example.nyam.data.remote.response.RegisterBody
import com.example.nyam.data.remote.response.UserData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: NyamRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<ResultState<UserData>>()
    val loginResult = _loginResult

    private val _registerResult = MutableLiveData<ResultState<PostResponse>>()
    val registerResult = _registerResult

    fun registerUser(userData: RegisterBody) {
        viewModelScope.launch {
            try {
                _registerResult.value = ResultState.Loading
                val response = repository.registerUser(userData)
                _registerResult.value = ResultState.Success(response)
            }
            catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, PostResponse::class.java)
                val errorMessage = errorBody.message
                _registerResult.value = errorMessage?.let { ResultState.Error(it) }
            }
            catch (e: Exception){
                _registerResult.value = ResultState.Error(e.message.toString())
            }
        }

    }

    fun getUser(id:String){
        viewModelScope.launch {
            try {
                _loginResult.value = ResultState.Loading
                val response = repository.getUser(id)
                _loginResult.value = ResultState.Success(response)
            }
            catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, UserData::class.java)
                val errorMessage = errorBody.email
                _loginResult.value = errorMessage?.let { ResultState.Error(it) }
            }
        }
    }
}