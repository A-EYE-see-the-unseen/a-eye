package com.dicoding.picodiploma.aeye.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dicoding.picodiploma.aeye.MainActivity
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
//    lateinit var sharedPreference:
    lateinit var sharedPref: SharedPref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        sharedPref = SharedPref(this)

        if (sharedPref.isLogin) Intent(this, DashboardActivity::class.java).apply {
            startActivity(this)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {

        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        when {
            email.isEmpty() -> {
                binding.etEmail.error = "Masukkan Email"
                binding.etEmail.requestFocus()
                return
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Masukkan Password"
                binding.etPassword.requestFocus()
                return
            }
        }

        ApiConfig.getApiService().login(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
//                            Toast.makeText(
//                                applicationContext,
//                                response.body()?.message,
//                                Toast.LENGTH_LONG
//                            ).show()

//                        SharedPref.getInstance(applicationContext)
//                            .saveUser(response.body()?.userResult!!)
                        sharedPref.isLogin = true

                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email atau Password Salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

}
