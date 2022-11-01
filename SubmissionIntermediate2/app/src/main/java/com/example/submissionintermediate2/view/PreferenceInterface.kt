package com.example.submissionintermediate2.view

import com.example.submissionintermediate2.model.UserModel
import kotlinx.coroutines.flow.Flow

interface PreferenceInterface {
    suspend fun setSessionUser(user: UserModel?)
    fun getSessionUser() : Flow<UserModel>

    suspend fun setIsDarkMode(isDarkMode: Boolean?)
    fun getIsDarkMode() : Flow<Boolean>
}