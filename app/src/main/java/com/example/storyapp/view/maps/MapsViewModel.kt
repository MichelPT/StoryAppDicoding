package com.example.storyapp.view.maps

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.R
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.response.ErrorResponse
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.di.Injection
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _listStoryLocations = MutableLiveData<List<ListStoryItem>>()
    val listStoryLocations: LiveData<List<ListStoryItem>> = _listStoryLocations


    fun getStoriesWithLocation()
    {
        viewModelScope.launch {
            try {
                val client = Injection.provideRepository(Application()).getStoriesWithLocation()
                Log.d("mainviewmodel", "session available")
                if (client.error != null && !client.error) {
                    _listStoryLocations.value = client.listStory
                    Log.d("mainviewmodel", "set all to false")
                }
            } catch (e: HttpException){
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            } catch (e: Exception){
            }
        }
    }
}