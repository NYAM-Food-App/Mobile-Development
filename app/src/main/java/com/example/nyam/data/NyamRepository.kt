package com.example.nyam.data

import com.example.nyam.data.pref.UserModel
import com.example.nyam.data.pref.UserPreference
import com.example.nyam.data.remote.response.UserData
import com.example.nyam.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class NyamRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun getUser(): UserData {
        return apiService.getUser()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: NyamRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): NyamRepository = instance ?: synchronized(this) {
            instance ?: NyamRepository(userPreference,apiService)
        }.also { instance = it }
    }
}