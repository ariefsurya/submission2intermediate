package com.example.submissionintermediate2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.submissionintermediate2.model.RemoteKeys
import com.example.submissionintermediate2.model.StoryModel

@Database(entities = [StoryModel::class, RemoteKeys::class], version = 2, exportSchema = false)
abstract class StoryRoomDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryRoomDatabase? = null

        fun getDatabase(context: Context): StoryRoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                    StoryRoomDatabase::class.java, "story_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}