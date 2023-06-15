package com.dicoding.picodiploma.aeye.ui.detecting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dicoding.picodiploma.aeye.ui.dashboard.DashboardActivity
import com.dicoding.picodiploma.aeye.ui.detail.DetailFragment
import com.dicoding.picodiploma.aeye.ui.detail.DetectedActivity
import com.dicoding.picodiploma.loginactivity.databinding.ActivityDetectingBinding
import io.socket.emitter.Emitter

class DetectingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectingBinding

    //private lateinit var detectingScreen: ConstraintLayout

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
        binding.detectedScreen.visibility = View.GONE

        binding.btnBerhenti.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            SocketHandler.closeConnection()
        }

        mSocket.on("hasil", object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                val img_url = args[0] as String
                Log.e("socketerino", img_url)
                DetectedActivity.newInstance(img_url)
                val intent = Intent(this@DetectingActivity, DetectedActivity::class.java)
                //intent.putExtra("ARG_IMG_LINK", detectionImage)
                startActivity(intent)
            }
        })

//        binding.btnSocketCheck.setOnClickListener{
//            mSocket.emit("hasil-req")
//            mSocket.on("hasil", object : Emitter.Listener {
//                override fun call(vararg args: Any?) {
//                    val img_url = args[0] as String
//                    Log.e("socketer", img_url)
//                }
//            })
//        }
//
//        binding.btnTestingNewDetection.setOnClickListener {
//            //mSocket.emit("detection-request")
//            mSocket.emit("send")
//            var detectionImage = "https://en.meming.world/images/en/d/d0/Crying_Cat.jpg"
//
//            mSocket.on("hasil", object : Emitter.Listener {
//                override fun call(vararg args: Any?) {
//                    detectionImage = args[0] as String
//                    DetectedActivity.newInstance(detectionImage)
//                }
//            })
//
//
//            val intent = Intent(this, DetectedActivity::class.java)
//            //intent.putExtra("ARG_IMG_LINK", detectionImage)
//            startActivity(intent)
    // }
    }

    fun toggleVisibility(show: Boolean){
        if (show) {
            binding.detectingScreen.visibility = View.VISIBLE
            binding.detectedScreen.visibility = View.GONE
        } else {
            binding.detectingScreen.visibility = View.GONE
            binding.detectedScreen.visibility = View.VISIBLE
        }
    }
}