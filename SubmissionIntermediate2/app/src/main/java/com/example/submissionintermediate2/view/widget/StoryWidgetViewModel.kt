package com.example.submissionintermediate2.view.widget

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import com.example.submissionintermediate2.model.ErrorResponseModel
import com.example.submissionintermediate2.model.StoryModel
import com.example.submissionintermediate2.model.UserModel
import com.example.submissionintermediate2.retrofit.ApiConfig
import com.example.submissionintermediate2.retrofit.response.StoryApiResponse
import com.example.submissionintermediate2.util.PreferenceManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class StoryWidgetViewModel(private val context: Context): ViewModel() {
//    private val mStoryRepository: StoryRepository = StoryRepository(application)
    private val mPreferenceManager: PreferenceManager = PreferenceManager.getInstance(context.dataStore)

    private val _currSessionUser = MutableLiveData<UserModel>()
    val currSessionUser: LiveData<UserModel> = _currSessionUser
    private val _storyList = MutableLiveData<ArrayList<StoryModel>>()
    val storyList: LiveData<ArrayList<StoryModel>> = _storyList

    fun getSessionUser(): LiveData<UserModel> {
        return mPreferenceManager.getSessionUser().asLiveData()
    }
    fun setSessionUser (userModel: UserModel?){
        viewModelScope.launch {
            mPreferenceManager.setSessionUser(userModel)
        }
    }
    fun setCurrentUser(currentUser: UserModel) {
        _currSessionUser.value = currentUser
    }

    fun doApiGetStories () {
        val responseBody = ApiConfig.getApiService().getStories("bearer " + currSessionUser.value?.token.toString())
        if (!responseBody.error) {
            _storyList.value = responseBody.listStory
        }
        else {
            Log.e(ContentValues.TAG, "onError Message: ${responseBody.message}")
        }
    }
}