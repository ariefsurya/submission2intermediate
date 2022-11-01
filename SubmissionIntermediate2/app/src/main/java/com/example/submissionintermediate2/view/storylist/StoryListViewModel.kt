package com.example.submissionintermediate2.view.storylist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submissionintermediate2.database.storyrepository.StoryRepository
import com.example.submissionintermediate2.di.Injection
import com.example.submissionintermediate2.model.StoryModel
import com.example.submissionintermediate2.model.UserModel
import kotlinx.coroutines.launch

class StoryListViewModel(private val storyRepository: StoryRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _currSessionUser = MutableLiveData<UserModel>()
    val currSessionUser: LiveData<UserModel> = _currSessionUser
    private val _storyList = MutableLiveData<PagingData<StoryModel>>()
    val storyList: LiveData<PagingData<StoryModel>> = _storyList


    class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StoryListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StoryListViewModel(Injection.provideRepository(context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    val quote: LiveData<PagingData<StoryModel>> =
        storyRepository.getAllStory("a").cachedIn(viewModelScope)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getSessionUser(): LiveData<UserModel> {
        return storyRepository.getSessionUser()
    }
    fun setSessionUser (userModel: UserModel?){
        viewModelScope.launch {
            storyRepository.setSessionUser(userModel)
        }
    }
    fun setCurrentUser(currentUser: UserModel) {
        _currSessionUser.value = currentUser
    }

    fun doApiGetStories () {
        _storyList.value = storyRepository.getAllStory(currSessionUser.value?.token ?: "").cachedIn(viewModelScope).value
    }
}