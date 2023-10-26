package com.example.storyapp.view.main

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.response.ErrorResponse
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.di.Injection
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listUsers: LiveData<List<ListStoryItem>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    val storyPager: LiveData<PagingData<ListStoryItem>> =
        repository.getStoriesPager().cachedIn(viewModelScope)

    var errorMessage = ""

    init {
        _isError.value = false
        _isLoading.value = true
    }

    fun getStories() {
        viewModelScope.launch {
            try {
                val client = Injection.provideRepository(Application()).getStories()
                if (client.error != null && !client.error) {
                    _listStories.value = client.listStory
                    _isError.value = false
                    _isLoading.value = false
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                errorMessage = errorBody.message
                _isError.postValue(true)
                _isLoading.value = false
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                _isError.postValue(true)
                _isLoading.value = false
            }
        }

    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}