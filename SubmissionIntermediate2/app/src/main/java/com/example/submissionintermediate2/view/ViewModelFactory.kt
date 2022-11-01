package com.example.submissionintermediate2.view

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionintermediate2.di.Injection
import com.example.submissionintermediate2.view.addstory.AddStoryViewModel
import com.example.submissionintermediate2.view.login.LoginViewModel
import com.example.submissionintermediate2.view.mapstorylist.MapStoryListViewModel
import com.example.submissionintermediate2.view.register.RegisterViewModel
import com.example.submissionintermediate2.view.storydetail.StoryDetailViewModel
import com.example.submissionintermediate2.view.storylist.StoryListViewModel
import com.example.submissionintermediate2.view.widget.StoryWidgetViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(var context: Context): ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(context)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(context) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(context) as T
            }
            modelClass.isAssignableFrom(StoryListViewModel::class.java) -> {
                StoryListViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(StoryDetailViewModel::class.java) -> {
                StoryDetailViewModel(context) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(context) as T
            }
            modelClass.isAssignableFrom(StoryWidgetViewModel::class.java) -> {
                StoryWidgetViewModel(context) as T
            }
            modelClass.isAssignableFrom(MapStoryListViewModel::class.java) -> {
                MapStoryListViewModel(context) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
        }
    }
}