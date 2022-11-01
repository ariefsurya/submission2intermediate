package com.example.submissionintermediate2.retrofit

import com.example.submissionintermediate2.model.UserModel
import com.example.submissionintermediate2.retrofit.response.ApiResponse
import com.example.submissionintermediate2.retrofit.response.LoginApiResponse
import com.example.submissionintermediate2.retrofit.response.StoryApiResponse
import com.example.submissionintermediate2.view.login.LoginViewModel.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun registerUser(@Body user: UserModel): Call<ApiResponse>

    @POST("login")
    fun login(@Body loginParameter: LoginParameter): Call<LoginApiResponse>

    @Multipart
    @POST("stories")
    fun addStories(@Header("Authorization") authorization: String,
                   @Part("description") description: String,
                   @Part photo: MultipartBody.Part): Call<ApiResponse>

    @GET("stories")
    fun getStories(@Header("Authorization") authorization: String,
                    @Query("page") page: Int = 1,
                    @Query("size") size: Int = 10,
                    @Query("location") location: Int = 0): StoryApiResponse
}