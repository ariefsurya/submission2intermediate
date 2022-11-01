package com.example.submissionintermediate2.view.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.submissionintermediate2.R
import com.example.submissionintermediate2.databinding.ActivityAddStoryBinding
import com.example.submissionintermediate2.util.createCustomTempFile
import com.example.submissionintermediate2.util.uriToFile
import com.example.submissionintermediate2.view.MainActivity
import com.example.submissionintermediate2.view.ViewModelFactory
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 100

        private var uploadedFile: File? = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
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
                finishAffinity()
                return true
            }
            else -> return true
        }
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this@AddStoryActivity.application)
        viewModel = ViewModelProvider(this@AddStoryActivity, factory).get(AddStoryViewModel::class.java)

        viewModel.getSessionUser().observe(this, { user ->
            if (user != null && !user.token.isNullOrEmpty()) {
                viewModel.setCurrentUser(user)
            }
        })
        viewModel.isLoading.observe(this, { isLoading ->
            showLoading(isLoading)
        })
        viewModel.apiResult.observe(this, { apiResult ->
            if (apiResult != null) {
                Toast.makeText(this, apiResult.message, Toast.LENGTH_SHORT).show()
                if (!apiResult.error) { finish() }
            }
        })
    }

    private fun setupAction() {
        binding.btnCamera.setOnClickListener{
            startTakePhoto()
        }
        binding.btnGallery.setOnClickListener{
            startGallery()
        }
        binding.buttonAdd.setOnClickListener{
            var isValid = true
            val description = binding.edAddDescription.text.toString()
            if (description.isEmpty() || description.isBlank()) {
                binding.edAddDescription.setError(getString(R.string.error_descriptiom_not_valid))
                isValid = false
            }
            else {
                binding.edAddDescription.setError(null)
            }
            if (uploadedFile == null) {
                Toast.makeText(this, getString(R.string.error_photo_not_valid), Toast.LENGTH_SHORT).show()
                isValid = false
            }
            if (isValid) {
                viewModel.doApiAddStories(description, uploadedFile!!)
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                this.packageName,
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }


    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            uploadedFile = myFile
            val result =  BitmapFactory.decodeFile(myFile.path)
            binding.previewImageView.setImageBitmap(result)
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            uploadedFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
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