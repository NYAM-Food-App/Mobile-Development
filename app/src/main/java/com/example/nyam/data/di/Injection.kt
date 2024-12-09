package com.example.nyam.data.di

import android.content.Context
import com.example.nyam.data.NyamRepository
import com.example.nyam.data.local.NyamDatabase
import com.example.nyam.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NyamRepository {
        val apiService = ApiConfig.getApiService()
        val database = NyamDatabase.getInstance(context)
        val recipesDao = database.recipesDao()
        return NyamRepository.getInstance(apiService,recipesDao)
    }
}