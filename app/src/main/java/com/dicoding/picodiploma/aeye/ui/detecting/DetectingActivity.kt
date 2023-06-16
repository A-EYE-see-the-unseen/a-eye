package com.dicoding.picodiploma.aeye.ui.detecting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.widget.Toast
import com.dicoding.picodiploma.aeye.ui.dashboard.DashboardActivity
import com.dicoding.picodiploma.aeye.ui.detail.DetectedActivity
import com.dicoding.picodiploma.loginactivity.databinding.ActivityDetectingBinding
import io.socket.emitter.Emitter
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import com.dicoding.picodiploma.aeye.data.response.InstanceResponse
import com.dicoding.picodiploma.aeye.data.retrofit.ApiConfig
import com.dicoding.picodiploma.loginactivity.R
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetectingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectingBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // INITIALIZE SOCKET CONNECTION
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        //


        binding = ActivityDetectingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Fetch Socket from SocketHandler
        val mSocket = SocketHandler.getSocket()

        supportActionBar?.hide()

        binding.btnBerhenti.setOnClickListener {
            showAreYouSureDialogStop()
        }

        //Creates new DetectedActivity upon new detection from socket
        mSocket.on("hasil", object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                val img_url = args[0] as String
                Log.e("socketerino", img_url)
                DetectedActivity.newInstance(img_url)
                val intent = Intent(this@DetectingActivity, DetectedActivity::class.java)
                startActivity(intent)
            }
        })

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAreYouSureDialogStop()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }

    private fun showAreYouSureDialogStop() {
        val builder = AlertDialog.Builder(this, R.style.CustomDialogAlert)
            .create()
        val view = layoutInflater.inflate(R.layout.layout_custom_dialog_stop_deteksi, null)

        val btnTidak = view.findViewById<Button>(R.id.btnTidakStopDeteksi)
        val btnLogout = view.findViewById<Button>(R.id.btnIyaStopDeteksi)

        builder.setView(view)
        btnTidak.setOnClickListener {
            builder.dismiss()
        }

        btnLogout.setOnClickListener {
            SocketHandler.closeConnection()
            stopInstance()
            builder.dismiss()
        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()
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
                    )
                        .show()
                    runBlocking {
                        val intent =
                            Intent(this@DetectingActivity, DashboardActivity::class.java)
                        startActivity(
                            intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this@DetectingActivity)
                                .toBundle()
                        )
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