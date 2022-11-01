package com.example.submissionintermediate2.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.submissionintermediate2.R
import com.example.submissionintermediate2.databinding.ActivityMainBinding
import com.example.submissionintermediate2.model.UserModel
import com.example.submissionintermediate2.util.StringUtil
import com.example.submissionintermediate2.view.login.LoginViewModel
import com.example.submissionintermediate2.view.register.RegisterActivity
import com.example.submissionintermediate2.view.storylist.StoryListActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sessionUser: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.sign_in)
        setupViewModel()
        setupAction()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.setting_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            else -> return true
        }
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this@MainActivity.application)
        loginViewModel = ViewModelProvider(this@MainActivity, factory).get(LoginViewModel::class.java)

        loginViewModel.isLoading.observe(this, { isLoading ->
            showLoading(isLoading)
        })
        loginViewModel.errorMessage.observe(this, { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })

        loginViewModel.getSessionUser().observe(this, { user ->
            this.sessionUser = user
            if (user != null && !user.token.isNullOrEmpty()) {
                val intent = Intent(this, StoryListActivity::class.java)
                startActivity(intent)
                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
                finish()
            }
        })
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            var isValid = true
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            if (email.isEmpty() || !StringUtil.isEmailValid(email)) {
                binding.edLoginEmail.setError(getString(R.string.error_email_not_valid))
                isValid = false
            }
            if (password.isEmpty() || password.trim().length < 6) {
                binding.edLoginPassword.setError(getString(R.string.error_password_min_length))
                isValid = false
            }
            if (isValid) {
                loginViewModel.doApiLogin(email, password)
            }
        }

        binding.tvRegisterBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            ActivityCompat.startActivity(this, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnLogin.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.btnLogin.isEnabled = true
            binding.progressBar.visibility = View.GONE
        }
    }
}