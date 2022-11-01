package com.example.submissionintermediate2.database

import androidx.recyclerview.widget.DiffUtil
import com.example.submissionintermediate2.model.StoryModel

class StoryDiffCallback(private val mOldStoryList: List<StoryModel>, private val mNewStoryList: List<StoryModel>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldStoryList.size
    }
    override fun getNewListSize(): Int {
        return mNewStoryList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldStoryList[oldItemPosition].id == mNewStoryList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldStory = mOldStoryList[oldItemPosition]
        val newStory = mNewStoryList[newItemPosition]
        return oldStory.id == newStory.id
    }
}