package com.example.storyapp

import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.response.ErrorResponse
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.LoginResult
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.response.StoryResponse


object DataDummy {

    fun generateDummyModel(): ListStoryItem {
        return ListStoryItem(
            id = "String",
            name = "string",
            description = "string",
            photoUrl = "string",
            createdAt = "string",
        )
    }

    fun generateDummyStoryEntity(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "this name $i",
                "this is description $i",
                "this is photoUrl $i",
                0.2,
                "this is createdAt $i",
                0.1
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyStoriesResponse(): StoryResponse {
        return StoryResponse( generateDummyStoryEntity(), false, "Success",)
    }

    private fun generateDummyLoginResult(): LoginResult {
        return LoginResult("123@gmail.com", "123456", "token")
    }

    fun generateDummyLoginResponse(): LoginResponse {
        return LoginResponse(loginResult = generateDummyLoginResult(), false, "Login successfully")
    }

    fun generateDummyRequestRegister(): LoginResult {
        return LoginResult("gira", "123@gmail.com", "1234567")
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            false,
            "success"
        )
    }

    fun generateDummyFileUploadResponse(): ErrorResponse {
        return ErrorResponse(
            false,
            "success"
        )
    }

}