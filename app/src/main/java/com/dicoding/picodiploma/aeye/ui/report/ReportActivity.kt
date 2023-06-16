package com.dicoding.picodiploma.aeye.ui.report

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dicoding.picodiploma.aeye.data.response.ReportResponse
import com.dicoding.picodiploma.aeye.data.retrofit.ApiConfig
import com.dicoding.picodiploma.aeye.data.storage.SharedPref
import com.dicoding.picodiploma.aeye.ui.dashboard.DashboardActivity
import com.dicoding.picodiploma.aeye.utils.AndroidDownloader
import com.dicoding.picodiploma.loginactivity.databinding.ActivityReportBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Laporan PDF"

        sharedPref = SharedPref(this)

        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ApiConfig.getApiService().getReport("Bearer ${sharedPref.getToken().toString()}")
            .enqueue(object : Callback<ReportResponse> {
                override fun onResponse(
                    call: Call<ReportResponse>,
                    response: Response<ReportResponse>
                ) {
//                    val downloader = AndroidDownloader(this)
//                downloader.downloadFile("https://s29.q4cdn.com/175625835/files/doc_downloads/test.pdf")

                    val downloader = AndroidDownloader(this@ReportActivity)
                    downloader.downloadFile("${response.body()?.url_pdf}")
                }

                override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()

                }
            })

        binding.btnOk.setOnClickListener {
            startActivity(Intent(this@ReportActivity, DashboardActivity::class.java))
            finish()
        }
    }
}