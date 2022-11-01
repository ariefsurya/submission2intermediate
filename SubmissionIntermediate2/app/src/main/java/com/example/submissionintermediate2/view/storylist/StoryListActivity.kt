package com.example.submissionintermediate2.view.storylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionintermediate2.R
//import com.example.submissionintermediate2.adapter.LoadingStateAdapter
import com.example.submissionintermediate2.adapter.StoryListAdapter
import com.example.submissionintermediate2.databinding.ActivityStoryListBinding
import com.example.submissionintermediate2.view.MainActivity
//import com.example.submissionintermediate2.view.ViewModelFactory
import com.example.submissionintermediate2.view.addstory.AddStoryActivity
import com.example.submissionintermediate2.view.mapstorylist.MapStoryListActivity

class StoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryListBinding
    private lateinit var storyListAdapter: StoryListAdapter
    private val viewModel: StoryListViewModel by viewModels {
        StoryListViewModel.ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.story)

        binding.rvStoryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        storyListAdapter = StoryListAdapter()
        binding.rvStoryList.adapter = storyListAdapter
//            .withLoadStateFooter(
//            footer = LoadingStateAdapter {
//                storyListAdapter.retry()
//            }
//        )
        setupViewModel()
        setupAction()
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
                finish()
                return true
            }
            else -> return true
        }
    }

    fun setupAction () {
        binding.fabAddStory.setOnClickListener({
            val intent = Intent(this, AddStoryActivity::class.java)
            ActivityCompat.startActivity(this, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        })
        binding.fabMap.setOnClickListener({
            val intent = Intent(this, MapStoryListActivity::class.java)
            ActivityCompat.startActivity(this, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        })
    }
    fun setupViewModel () {
//        val factory = ViewModelFactory.getInstance(this@StoryListActivity.application)
//        viewModel = ViewModelProvider(this@StoryListActivity, factory).get(StoryListViewModel::class.java)

        viewModel.getSessionUser().observe(this, { user ->
            if (user != null && !user.token.isNullOrEmpty()) {
                viewModel.setCurrentUser(user)
                viewModel.doApiGetStories()
            }
        })
        viewModel.isLoading.observe(this, { isLoading ->
            showLoading(isLoading)
        })

        viewModel.errorMessage.observe(this, { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                binding.tvNotFound.text = errorMessage
                binding.tvNotFound.visibility = View.VISIBLE
            }
            else {
                binding.tvNotFound.text = ""
                binding.tvNotFound.visibility = View.GONE
            }
        })

        viewModel.storyList.observe(this, { storyList ->
            if (storyList != null) {
                storyListAdapter.submitData(lifecycle, storyList)
            }
        })

//        lifecycleScope.launch {
//            storyListAdapter.loadStateFlow
//                .map { it.refresh }
//                .distinctUntilChanged()
//                .collectLatest {
//                    // show a retry button outside the list when refresh hits an error
////                    retryButton.isVisible = it is LoadState.Error
//
//                    // swipeRefreshLayout displays whether refresh is occurring
////                    swipeRefreshLayout.isRefreshing = it is LoadState.Loading
//
//                    // show an empty state over the list when loading initially, before items are loaded
//                    if (it is LoadState.Loading && storyListAdapter.itemCount == 0) {
//                        binding.tvNotFound.visibility = View.VISIBLE
//                    }
//                    else {
//                        binding.tvNotFound.visibility = View.GONE
//                    }
//                }
//        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}