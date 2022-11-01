package com.example.submissionintermediate2.view.widget

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.submissionintermediate2.R
import com.example.submissionintermediate2.database.StoryRoomDatabase
import com.example.submissionintermediate2.database.storyrepository.StoryRepository
import com.example.submissionintermediate2.model.StoryModel
import java.io.IOException
import java.lang.Exception
import java.net.URL

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {
    private val mStoryRoomDatabase: StoryRoomDatabase = StoryRoomDatabase.getDatabase(mContext)
//    private val mStoryRepository: StoryRepository = StoryRepository(mContext as Application)
    private var storyList: List<StoryModel>? = listOf<StoryModel>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        storyList = mStoryRoomDatabase.storyDao().getAllStoryDatabase()
    }

    fun URL.toBitmap(): Bitmap?{
        return try {
            BitmapFactory.decodeStream(openStream())
        }catch (e: IOException){
            null
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = storyList?.size ?: 0

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.story_widget_item)
        try {
            val bitmap: Bitmap = Glide.with(mContext.applicationContext)
                .asBitmap()
                .load(storyList?.get(position)?.photoUrl ?: "")
                .submit()
                .get()
            rv.setImageViewBitmap(R.id.image_view, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val extras = bundleOf(
            StoryListWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.image_view, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}