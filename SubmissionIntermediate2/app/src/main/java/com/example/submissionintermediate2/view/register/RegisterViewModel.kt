package com.example.submissionintermediate2.view.register

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import com.example.submissionintermediate2.R
import com.example.submissionintermediate2.model.ErrorResponseModel
import com.example.submissionintermediate2.model.UserModel
import com.example.submissionintermediate2.retrofit.ApiConfig
import com.example.submissionintermediate2.retrofit.response.ApiResponse
import com.example.submissionintermediate2.util.PreferenceManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class RegisterViewModel(private val context: Context) : ViewModel() {
    private val mPreferenceManager: PreferenceManager = PreferenceManager.getInstance(context.dataStore)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isRegisterSuccess = MutableLiveData<Boolean>()
    val isRegisterSuccess: LiveData<Boolean> = _isRegisterSuccess
    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = _responseMessage

    fun getIsDarkMode(): LiveData<Boolean> {
        return mPreferenceManager.getIsDarkMode().asLiveData()
    }
    fun setIsDarkMode (isDarkMode: Boolean) {
        viewModelScope.launch {
            mPreferenceManager.setIsDarkMode(isDarkMode)
        }
    }

    fun doApiRegisterUser (name: String, email: String, password: String) {
        _isLoading.value = true
        val oUser = UserModel(name = name, email = email, password = password)
        val client = ApiConfig.getApiService().registerUser(oUser)
        client.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (!responseBody.error) {
                        _responseMessage.value = context.getString(R.string.register_success)
                        _isRegisterSuccess.value = true
                    }
                    else {
                        Log.e(ContentValues.TAG, "onError Message: ${responseBody.message}")
                        _responseMessage.value = responseBody.message
                    }
                }
                else {
                    val responseError = Gson().fromJson(response.errorBody()?.charStream(), ErrorResponseModel::class.java)
                    _responseMessage.value = responseError.message
                    Log.e(ContentValues.TAG, "onFailure: ${responseBody?.message}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _isLoading.value = false
                _responseMessage.value = t.message
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}