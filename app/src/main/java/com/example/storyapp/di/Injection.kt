package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        val apiService = ApiConfig.getApiService(token)
        return UserRepository.getInstance(pref, apiService)
    }
}