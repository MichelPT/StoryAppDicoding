package com.example.storyapp.data

import android.app.Application
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.di.Injection

class StoryPagingSource(private val apiService: ApiService) :
    PagingSource<Int, ListStoryItem>() {
    private val listStoryItemCache = mutableListOf<ListStoryItem>()
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        listStoryItemCache.clear()
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoriesWithLocation(position, params.loadSize).listStory
            listStoryItemCache.addAll(responseData)
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    suspend fun refresh(){
        listStoryItemCache.clear()
        load(LoadParams.Refresh(key = null, 3, false))
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }


}