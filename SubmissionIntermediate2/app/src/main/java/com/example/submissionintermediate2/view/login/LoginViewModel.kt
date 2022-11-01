package com.example.submissionintermediate2.view.login

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import com.example.submissionintermediate2.model.ErrorResponseModel
import com.example.submissionintermediate2.model.UserModel
import com.example.submissionintermediate2.retrofit.ApiConfig
import com.example.submissionintermediate2.retrofit.response.LoginApiResponse
import com.example.submissionintermediate2.util.PreferenceManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class LoginViewModel(private val context: Context): ViewModel() {
    private val mPreferenceManager: PreferenceManager = PreferenceManager.getInstance(context.dataStore)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    class LoginParameter(val email: String, val password: String)

    fun setSessionUser (userModel: UserModel) {
        viewModelScope.launch {
            mPreferenceManager.setSessionUser(userModel)
        }
    }
    fun getSessionUser(): LiveData<UserModel> {
        return mPreferenceManager.getSessionUser().asLiveData()
    }

    fun doApiLogin (email: String, password: String) {
        _isLoading.value = true
        val loginParameter = LoginParameter(email, password)
        val client = ApiConfig.getApiService().login(loginParameter)
        client.enqueue(object : Callback<LoginApiResponse> {
            override fun onResponse(call: Call<LoginApiResponse>, response: Response<LoginApiResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (!responseBody.error) {
                        setSessionUser(responseBody.loginResult)
                    }
                    else {
                        Log.e(ContentValues.TAG, "onError Message: ${responseBody.message}")
                        _errorMessage.value = responseBody.message
                    }
                }
                else {
                    try {
                        val responseError = Gson().fromJson(response.errorBody()?.charStream(), ErrorResponseModel::class.java)
                        _errorMessage.value = responseError.message
                        Log.e(ContentValues.TAG, "onFailure: ${_errorMessage.value}")
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onFailure(call: Call<LoginApiResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}