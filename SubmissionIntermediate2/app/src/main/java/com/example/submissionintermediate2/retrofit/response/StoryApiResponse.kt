package com.example.submissionintermediate2.retrofit.response

import com.example.submissionintermediate2.model.StoryModel
import com.google.gson.annotations.SerializedName

data class StoryApiResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val listStory: ArrayList<StoryModel>
)
