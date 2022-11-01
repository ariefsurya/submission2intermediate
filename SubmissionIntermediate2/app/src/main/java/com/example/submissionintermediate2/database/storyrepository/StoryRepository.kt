package com.example.submissionintermediate2.database.storyrepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.*
import com.example.submissionintermediate2.database.StoryRoomDatabase
import com.example.submissionintermediate2.model.StoryModel
import com.example.submissionintermediate2.model.UserModel
import com.example.submissionintermediate2.retrofit.ApiService
import com.example.submissionintermediate2.util.PreferenceManager

class StoryRepository(private val preferenceManager: PreferenceManager, private val storyRoomDatabase: StoryRoomDatabase, private val apiService: ApiService) {
    fun getAllStory(token: String): LiveData<PagingData<StoryModel>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(storyRoomDatabase, apiService, token),
            pagingSourceFactory = {
                storyRoomDatabase.storyDao().getAllStoryDB()
            }
        ).liveData
    }

    suspend fun setSessionUser (userModel: UserModel?){
        preferenceManager.setSessionUser(userModel)
    }
    fun getSessionUser (): LiveData<UserModel> {
        return preferenceManager.getSessionUser().asLiveData()
    }
}