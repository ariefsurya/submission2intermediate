package com.example.submissionintermediate2.view.storydetail

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import com.example.submissionintermediate2.model.UserModel
import com.example.submissionintermediate2.util.PreferenceManager
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class StoryDetailViewModel(private val context: Context): ViewModel() {
    private val mPreferenceManager: PreferenceManager = PreferenceManager.getInstance(context.dataStore)

    fun setSessionUser (userModel: UserModel?){
        viewModelScope.launch {
            mPreferenceManager.setSessionUser(userModel)
        }
    }
}