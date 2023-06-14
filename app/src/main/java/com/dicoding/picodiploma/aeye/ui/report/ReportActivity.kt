package com.dicoding.picodiploma.aeye.ui.report

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dicoding.picodiploma.aeye.data.response.ReportResponse
import com.dicoding.picodiploma.aeye.data.retrofit.ApiConfig
import com.dicoding.picodiploma.aeye.data.storage.SharedPref
import com.dicoding.picodiploma.loginactivity.databinding.ActivityReportBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = SharedPref(this)

        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApiConfig.getApiService().getReport("Bearer ".plus(sharedPref.getToken()))
            .enqueue(object : Callback<ReportResponse> {
                override fun onResponse(
                    call: Call<ReportResponse>,
                    response: Response<ReportResponse>
                ) {
                    Toast.makeText(applicationContext, "API Response !", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()

                }
            })
    }
}