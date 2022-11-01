package com.example.submissionintermediate2.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.submissionintermediate2.model.StoryModel

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(story: List<StoryModel>)

    @Query("SELECT * from StoryModel")
    fun getAllStoryDB(): PagingSource<Int, StoryModel>

    @Query("SELECT * from StoryModel")
    fun getAllStoryDatabase(): List<StoryModel>?

    @Query("DELETE FROM StoryModel")
    suspend fun deleteAll()
}