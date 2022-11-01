package com.example.submissionintermediate2.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.submissionintermediate2.R
import com.example.submissionintermediate2.databinding.ActivityRegisterBinding
import com.example.submissionintermediate2.util.StringUtil
import com.example.submissionintermediate2.view.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
//    private var mIsDarkMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupViewModel()
        setupAction()
    }
    fun setupActionBar () {
        val actionbar = supportActionBar
        supportActionBar?.title = getString(R.string.sign_up)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.setting_menu, menu)
        return true
    }
//    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
//        super.onPrepareOptionsMenu(menu)
//        if (mIsDarkMode) {
//            menu.findItem(R.id.appearance_setting).setTitle(getString(R.string.light_mode))
//        }
//        else {
//            menu.findItem(R.id.appearance_setting).setTitle(getString(R.string.dark_mode))
//        }
//        return true
//    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.appearance_setting -> {
//                if (mIsDarkMode) {
//                    viewModel.setIsDarkMode(false)
//                    item.setTitle(getString(R.string.dark_mode))
//                }
//                else {
//                    viewModel.setIsDarkMode(true)
//                    item.setTitle(getString(R.string.light_mode))
//                }
//                return true
//            }
            R.id.language_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            else -> return true
        }
    }

    fun setupViewModel () {
        val factory = ViewModelFactory.getInstance(this@RegisterActivity.application)
        viewModel = ViewModelProvider(this@RegisterActivity, factory).get(RegisterViewModel::class.java)

        viewModel.isLoading.observe(this, { isLoading ->
            showLoading(isLoading)
        })
        viewModel.responseMessage.observe(this, { responseMessage ->
            Toast.makeText(this, responseMessage, Toast.LENGTH_SHORT).show()
        })
        viewModel.isRegisterSuccess.observe(this, { isRegisterSuccess ->
            this.finish()
        })
//        viewModel.getIsDarkMode().observe(this, { isDarkMode ->
//            mIsDarkMode = isDarkMode
//            if (isDarkMode) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            }
//            else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//            delegate.applyDayNight()
//        })
    }

    fun setupAction () {
        binding.btnSignUp.setOnClickListener {
            var isValid = true
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            if (name.isEmpty()) {
                binding.edRegisterName.setError(getString(R.string.error_name_not_valid))
                isValid = false
            }
            if (email.isEmpty() || !StringUtil.isEmailValid(email)) {
                binding.edRegisterEmail.setError(getString(R.string.error_email_not_valid))
                isValid = false
            }
            if (password.isEmpty() || password.length < 6) {
                binding.edRegisterPassword.setError(getString(R.string.error_password_min_length))
                isValid = false
            }
            if (isValid) {
                viewModel.doApiRegisterUser(name, email, password)
                this.finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSignUp.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.btnSignUp.isEnabled = true
            binding.progressBar.visibility = View.GONE
        }
    }
}
