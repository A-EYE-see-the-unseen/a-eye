package com.dicoding.picodiploma.aeye.ui.dashboard

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.dicoding.picodiploma.aeye.data.response.InstanceResponse
import com.dicoding.picodiploma.aeye.data.response.LogoutResponse
import com.dicoding.picodiploma.aeye.data.retrofit.ApiConfig
import com.dicoding.picodiploma.aeye.data.storage.SharedPref
import com.dicoding.picodiploma.aeye.ui.detecting.DetectingActivity
import com.dicoding.picodiploma.aeye.ui.login.LoginActivity
import com.dicoding.picodiploma.aeye.ui.report.ReportActivity
import com.dicoding.picodiploma.loginactivity.R
import com.dicoding.picodiploma.loginactivity.databinding.ActivityDashboardBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPref = SharedPref(this)

        val btnDetecting: Button = binding.btnStartDetecting
        btnDetecting.setOnClickListener(this)

        val btnReportPDF: Button = binding.btnReportPDF
        btnReportPDF.setOnClickListener(this)

        val btnLogout: Button = binding.btnLogout
        btnLogout.setOnClickListener(this)

        binding.apply {
            btnStartDetecting.isEnabled = true
            btnReportPDF.isEnabled = true
            btnLogout.isEnabled = true
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAreYouSureDialog()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }

    private fun showAreYouSureDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomDialogAlert)
            .create()
        val view = layoutInflater.inflate(R.layout.layout_custom_dialog_logout, null)

        val btnTidak = view.findViewById<Button>(R.id.btnTidak)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        builder.setView(view)
        btnTidak.setOnClickListener {
            builder.dismiss()
        }

        btnLogout.setOnClickListener {
            stopInstance()
            logout()
            builder.dismiss()
        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnStartDetecting -> {
                startInstanceDialog()
            }
            R.id.btnReportPDF -> {
                startActivity(Intent(this@DashboardActivity, ReportActivity::class.java))
            }
            R.id.btnLogout -> {
                showAreYouSureDialog()
            }
        }
    }

    private fun logout() {
        ApiConfig.getApiService().logout("Bearer ${sharedPref.getToken().toString()}")
            .enqueue(object : Callback<LogoutResponse> {
                override fun onResponse(
                    call: Call<LogoutResponse>,
                    response: Response<LogoutResponse>
                ) {
                    sharedPref.clearToken()
                    startActivity(
                        Intent(this@DashboardActivity, LoginActivity::class.java),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this@DashboardActivity)
                            .toBundle())
                    finish()
                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    Log.e(TAG, "Logout Gagal")
                }

            })
    }

    private fun startInstanceDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomDialogAlert)
            .create()
        val view = layoutInflater.inflate(R.layout.layout_custom_dialog_deteksi, null)

        val btnTidakDeteksi = view.findViewById<Button>(R.id.btnTidakDeteksi)
        val btnIyaDeteksi = view.findViewById<Button>(R.id.btnIyaDeteksi)

        builder.setView(view)
        btnTidakDeteksi.setOnClickListener {
            builder.dismiss()
        }

        btnIyaDeteksi.setOnClickListener {
            startInstance()
            binding.progressBar.visibility = View.VISIBLE
            binding.apply {
                btnReportPDF.isEnabled = false
                btnLogout.isEnabled = false
                btnStartDetecting.isEnabled = false
            }
            builder.dismiss()
        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun startInstance() {
        //Start-Instance
        ApiConfig.getApiService().startInstance()
            .enqueue(object : Callback<InstanceResponse> {
                override fun onResponse(
                    call: Call<InstanceResponse>,
                    response: Response<InstanceResponse>
                ) {
                    Toast.makeText(
                        applicationContext,
                        "Instance Berhasil Dimulai !",
                        Toast.LENGTH_SHORT
                    ).show()
                    runBlocking {
                        Toast.makeText(
                            applicationContext,
                            "Mengalihkan ke Deteksi ...",
                            Toast.LENGTH_SHORT
                        ).show()
                        delay(3000)
                        binding.progressBar.visibility = View.INVISIBLE
                        val intent =
                            Intent(this@DashboardActivity, DetectingActivity::class.java)
                        startActivity(
                            intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this@DashboardActivity)
                                .toBundle()
                        )
                    }
                }

                override fun onFailure(call: Call<InstanceResponse>, t: Throwable) {
                    // Handle network failures or exceptions
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(
                        applicationContext,
                        "Cek koneksi internet anda.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }

    fun stopInstance() {
        //Stop Instance
        ApiConfig.getApiService().stopInstance()
            .enqueue(object : Callback<InstanceResponse> {
                override fun onResponse(
                    call: Call<InstanceResponse>,
                    response: Response<InstanceResponse>
                ) {
                    Toast.makeText(
                        applicationContext,
                        "Instance Berhasil Dihentikan.",
                        Toast.LENGTH_SHORT
                    ).show()
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