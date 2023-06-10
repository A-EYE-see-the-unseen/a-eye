package com.dicoding.picodiploma.aeye.ui.detecting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dicoding.picodiploma.aeye.ui.dashboard.DashboardActivity
import com.dicoding.picodiploma.aeye.ui.detail.DetailFragment
import com.dicoding.picodiploma.aeye.ui.detail.DetectedActivity
import com.dicoding.picodiploma.loginactivity.databinding.ActivityDetectingBinding

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
        }

        binding.btnTestingNewDetection.setOnClickListener {
            var detectionImage = "https://en.meming.world/images/en/d/d0/Crying_Cat.jpg"
            DetectedActivity.newInstance(detectionImage)
//            val detectionDetailsFragmentManager = supportFragmentManager
//            detectionDetailsFragmentManager.commit{
//                replace(R.id.container, detectionDetailsFragment, DetailFragment::class.java.simpleName)
//                addToBackStack(null)
//            }

//            toggleVisibility(false)
            val intent = Intent(this, DetectedActivity::class.java)
//            intent.putExtra("key", detectionDetailsFragment)
            startActivity(intent)
        }
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