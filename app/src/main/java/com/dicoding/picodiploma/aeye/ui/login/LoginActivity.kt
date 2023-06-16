package com.dicoding.picodiploma.aeye.ui.login

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dicoding.picodiploma.aeye.data.response.LoginResponse
import com.dicoding.picodiploma.aeye.data.retrofit.ApiConfig
import com.dicoding.picodiploma.aeye.data.storage.SharedPref
import com.dicoding.picodiploma.aeye.ui.dashboard.DashboardActivity
import com.dicoding.picodiploma.loginactivity.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
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
                password.length < 6 -> {
                    binding.etPassword.error = "Password harus terdiri dari setidaknya 6 karakter"
                    binding.etPassword.requestFocus()
                }
                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnLogin.isEnabled = false
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
                        val sessionManager = SharedPref(applicationContext)
                        val token = response.body()?.accessToken
//                        val idPengawas = response.body()?.userResult?.id_pengawas

                        token?.let { sessionManager.saveToken(token)}
//                        idPengawas?.let { sessionManager.saveId(idPengawas) }

                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btnLogin.isEnabled = true
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
                        finish()
                    } else {
                        binding.tvDeniedMessage.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btnLogin.isEnabled = true
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(applicationContext, "Cek koneksi internet anda.", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}
