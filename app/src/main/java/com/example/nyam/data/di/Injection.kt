package com.example.nyam.data.di

import android.content.Context
import com.example.nyam.data.NyamRepository
import com.example.nyam.data.pref.UserPreference
import com.example.nyam.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): NyamRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return NyamRepository.getInstance(pref)
    }
}