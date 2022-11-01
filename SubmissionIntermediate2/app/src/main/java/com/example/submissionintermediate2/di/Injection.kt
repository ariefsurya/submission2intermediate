package com.example.submissionintermediate2.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.submissionintermediate2.database.StoryRoomDatabase
import com.example.submissionintermediate2.database.storyrepository.StoryRepository
import com.example.submissionintermediate2.retrofit.ApiConfig
import com.example.submissionintermediate2.util.PreferenceManager

object Injection {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
    fun provideRepository(context: Context): StoryRepository {
        val preferenceManager = PreferenceManager.getInstance(context.dataStore)
        val database = StoryRoomDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(preferenceManager, database, apiService)
    }
}