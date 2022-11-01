package com.example.submissionintermediate2.retrofit.response

import com.example.submissionintermediate2.model.UserModel
import com.google.gson.annotations.SerializedName

data class LoginApiResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: UserModel
)