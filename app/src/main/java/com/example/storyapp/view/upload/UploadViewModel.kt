package com.example.storyapp.view.upload

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import java.io.File

class UploadViewModel(private val repository: UserRepository): ViewModel() {
    suspend fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}