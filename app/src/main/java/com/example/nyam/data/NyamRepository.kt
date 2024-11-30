package com.example.nyam.data

import com.example.nyam.data.pref.UserModel
import com.example.nyam.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class NyamRepository private constructor(
    private val userPreference: UserPreference
) {

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
            userPreference: UserPreference
        ): NyamRepository = instance ?: synchronized(this) {
            instance ?: NyamRepository(userPreference)
        }.also { instance = it }
    }
}