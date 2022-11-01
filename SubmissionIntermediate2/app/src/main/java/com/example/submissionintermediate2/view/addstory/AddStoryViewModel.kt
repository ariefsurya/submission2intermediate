package com.example.submissionintermediate2.view.addstory

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import com.example.submissionintermediate2.model.ErrorResponseModel
import com.example.submissionintermediate2.model.UserModel
import com.example.submissionintermediate2.retrofit.ApiConfig
import com.example.submissionintermediate2.retrofit.response.ApiResponse
import com.example.submissionintermediate2.util.PreferenceManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Exception

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class AddStoryViewModel(private val context: Context): ViewModel() {
    private val mPreferenceManager: PreferenceManager = PreferenceManager.getInstance(context.dataStore)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _currSessionUser = MutableLiveData<UserModel>()
    val currSessionUser: LiveData<UserModel> = _currSessionUser
    private val _apiResult = MutableLiveData<ApiResponse?>()
    val apiResult: LiveData<ApiResponse?> = _apiResult

    fun getSessionUser(): LiveData<UserModel> {
        _currSessionUser.value = mPreferenceManager.getSessionUser().asLiveData().value
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

    fun doApiAddStories (description: String, photo: File) {
        _isLoading.value = true

        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestImageFile
        )
        val client = ApiConfig.getApiService().addStories("bearer " + currSessionUser.value?.token.toString(),
            description, imageMultipart)
        client.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                _isLoading.value = false
                var responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _apiResult.value = responseBody
                    if (!responseBody.error) {
                        Toast.makeText(context, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Log.e(ContentValues.TAG, "onError Message: ${responseBody.message}")
                        Toast.makeText(context, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    try {
                        val responseError = Gson().fromJson(response.errorBody()?.charStream(), ErrorResponseModel::class.java)
                        responseBody = ApiResponse(true, responseError.message)
                        _apiResult.value = responseBody
                        Log.e(ContentValues.TAG, "onFailure: ${responseError.message}")
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _isLoading.value = false
                val responseBody = ApiResponse(true, t.message?: "")
                _apiResult.value = responseBody
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}
