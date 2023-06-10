package com.dicoding.picodiploma.aeye.ui.detecting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dicoding.picodiploma.aeye.data.response.InstanceResponse
import com.dicoding.picodiploma.aeye.data.retrofit.ApiConfig
import com.dicoding.picodiploma.aeye.ui.dashboard.DashboardActivity
import com.dicoding.picodiploma.loginactivity.databinding.ActivityDetectingBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetectingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetectingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        binding.tvSearching.text = "Searching"
        GlobalScope.launch(Dispatchers.Main) {
            var dots = " . "
            while (true) {
                delay(1000) // Delay 1 second
                dots += "."
                if (dots.length > 3) {
                    dots = "." // Reset titik
                }
                binding.tvSearching.text = "Searching $dots"
            }
        }

        binding.btnBerhenti.setOnClickListener {
            ApiConfig.getApiService().stopInstance()
                .enqueue(object : Callback<InstanceResponse> {
                    override fun onResponse(
                        call: Call<InstanceResponse>,
                        response: Response<InstanceResponse>
                    ) {
                        Toast.makeText(applicationContext, "Instance Berhasil Dihentikan.", Toast.LENGTH_SHORT)
                            .show()
                        runBlocking {
                            delay(2000)
                            val intent =
                                Intent(this@DetectingActivity, DashboardActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<InstanceResponse>, t: Throwable) {
                        // Handle network failures or exceptions
                        Toast.makeText(
                            applicationContext,
                            "Cek koneksi internet anda.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })

        }
    }
}