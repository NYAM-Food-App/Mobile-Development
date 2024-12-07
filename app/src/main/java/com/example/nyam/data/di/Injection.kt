package com.example.nyam.data.di

import android.content.Context
import com.example.nyam.data.NyamRepository
import com.example.nyam.data.local.NyamDatabase
import com.example.nyam.data.pref.UserPreference
import com.example.nyam.data.pref.dataStore
import com.example.nyam.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NyamRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val database = NyamDatabase.getInstance(context)
        val recipesDao = database.recipesDao()
        return NyamRepository.getInstance(pref,apiService,recipesDao)
    }
}