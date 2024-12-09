package com.example.nyam.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nyam.data.NyamRepository
import com.example.nyam.data.ResultState
import com.example.nyam.data.remote.response.DailyNeeds
import com.example.nyam.data.remote.response.UserData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(private val repository: NyamRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<ResultState<UserData>>()
    val loginResult = _loginResult

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