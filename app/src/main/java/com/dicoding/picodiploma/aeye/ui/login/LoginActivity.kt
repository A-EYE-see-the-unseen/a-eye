package com.dicoding.picodiploma.aeye.ui.login

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dicoding.picodiploma.aeye.data.response.LoginResponse
import com.dicoding.picodiploma.aeye.data.retrofit.ApiConfig
import com.dicoding.picodiploma.aeye.data.storage.SharedPref
import com.dicoding.picodiploma.aeye.ui.dashboard.DashboardActivity
import com.dicoding.picodiploma.loginactivity.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        sharedPref = SharedPref(this)
        isNetworkAvailable(this)

        val savedToken = sharedPref.getToken()

        if (savedToken != null) Intent(this, DashboardActivity::class.java).apply {
            startActivity(this)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            when {
                email.isEmpty() -> {
                    binding.etEmail.error = "Masukkan Email"
                    binding.etEmail.requestFocus()
                }
                password.isEmpty() -> {
                    binding.etPassword.error = "Masukkan Password"
                    binding.etPassword.requestFocus()
                }
                password.length < 8 -> {
                    binding.etPassword.error = "Password harus terdiri dari setidaknya 8 karakter"
                    binding.etPassword.requestFocus()
                }
                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    login(email, password)
                }
            }
        }

    }

    private fun login(email: String, password: String) {
        ApiConfig.getApiService().login(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val tokenManager = SharedPref(applicationContext)
                        val token = response.body()?.token
                        token?.let { tokenManager.saveToken(token)}

                        binding.progressBar.visibility = View.INVISIBLE
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        binding.tvDeniedMessage.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Cek koneksi internet anda.", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw      = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

}
