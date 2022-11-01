package com.example.submissionintermediate2.view.mapstorylist

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import com.example.submissionintermediate2.R
import com.example.submissionintermediate2.databinding.ActivityMapStoryListBinding
import com.example.submissionintermediate2.databinding.ActivityStoryListBinding
import com.example.submissionintermediate2.model.StoryModel
import com.example.submissionintermediate2.view.ViewModelFactory
import com.example.submissionintermediate2.view.storylist.StoryListViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapStoryListActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapStoryListBinding
    private lateinit var viewModel: MapStoryListViewModel
    private var zoom = 10f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        supportActionBar?.title = getString(R.string.map_story)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setupViewModel()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_story) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun setupViewModel () {
        val factory = ViewModelFactory.getInstance(this@MapStoryListActivity.application)
        viewModel = ViewModelProvider(this@MapStoryListActivity, factory).get(MapStoryListViewModel::class.java)

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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        viewModel.getSessionUser().observe(this, { user ->
            if (user != null && !user.token.isNullOrEmpty()) {
                viewModel.setCurrentUser(user)
                viewModel.doApiGetStories()
            }
        })

        viewModel.storyList.observe(this, { storyList ->
            if (storyList.isEmpty()) {
                binding.tvNotFound.visibility = View.VISIBLE
            }
            else {
                binding.tvNotFound.visibility = View.GONE
                addStoryListMarker(storyList)
            }
        })
    }

    private fun addStoryListMarker(storyList: ArrayList<StoryModel>) {
        storyList.forEachIndexed { index, story ->
            val latlngPosition = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
            mMap.addMarker(
                MarkerOptions()
                    .position(latlngPosition)
                    .title(story.name)
                    .snippet(story.description)
            )
            if (index == storyList.size -1) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlngPosition, zoom))
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}