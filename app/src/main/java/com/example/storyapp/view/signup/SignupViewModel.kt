package com.example.storyapp.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.response.ErrorResponse
import com.example.storyapp.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    private val _errorResult = MutableLiveData<ErrorResponse>()
    val errorResult : LiveData<ErrorResponse> get() = _errorResult

    fun signup(name:String, email:String, password:String){
        viewModelScope.launch {
            Log.d("registerresponse", "registered")
            try {
                val response = repository.register(name, email, password)
                val message = response.message
                _errorResult.postValue(ErrorResponse(false, message))
            } catch (e: HttpException){
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                _errorResult.postValue(errorBody)
            } catch (e: Exception){
                _errorResult.postValue(ErrorResponse(true, "Unknown error!"))
            }
        }
    }
}