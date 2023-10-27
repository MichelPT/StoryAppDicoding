package com.example.storyapp.view.maps

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.di.Injection
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _listStoryLocations = MutableLiveData<List<ListStoryItem>>()
    val listStoryLocations: LiveData<List<ListStoryItem>> = _listStoryLocations

    var errorMessage = ""
    init {
        getStoriesWithLocation()
    }

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            try {
                val storyList = Injection.provideRepository(Application()).getStoriesWithLocation().listStory
                _listStoryLocations.value = storyList
            } catch (e:Exception){
                errorMessage = e.message.toString()
            }
        }
    }
}