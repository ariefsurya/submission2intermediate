package com.example.submissionintermediate2.view.storydetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.submissionintermediate2.R
import com.example.submissionintermediate2.databinding.ActivityStoryDetailBinding
import com.example.submissionintermediate2.model.StoryModel
import com.example.submissionintermediate2.view.MainActivity
import com.example.submissionintermediate2.view.ViewModelFactory

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private lateinit var viewModel: StoryDetailViewModel

    companion object {
        const val STORY_DETAIL = "STORY_DETAIL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.story_detail)
        setupData()
        setupViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.story_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            R.id.logout -> {
                viewModel.setSessionUser(null)
                val intent = Intent(this, MainActivity::class.java)
                ActivityCompat.startActivity(this, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
                finishAffinity()
                return true
            }
            else -> return true
        }
    }

    private fun setupData() {
        val story = intent.getParcelableExtra<StoryModel>(STORY_DETAIL) as StoryModel
        Glide.with(applicationContext)
            .load(story.photoUrl)
            .circleCrop()
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = story.name
        binding.tvDetailDescription.text = story.description
    }

    fun setupViewModel () {
        val factory = ViewModelFactory.getInstance(this@StoryDetailActivity.application)
        viewModel = ViewModelProvider(this@StoryDetailActivity, factory).get(StoryDetailViewModel::class.java)
    }
}