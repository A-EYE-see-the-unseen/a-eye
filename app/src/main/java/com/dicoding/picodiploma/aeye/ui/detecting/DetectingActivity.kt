package com.dicoding.picodiploma.aeye.ui.detecting

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.commit
import com.dicoding.picodiploma.aeye.ui.dashboard.DashboardActivity
import com.dicoding.picodiploma.aeye.ui.detail.DetailFragment
import com.dicoding.picodiploma.loginactivity.R
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

        binding.btnBerhenti.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        binding.btnTestingNewDetection.setOnClickListener {

            var detectionImage: String = "https://en.meming.world/images/en/d/d0/Crying_Cat.jpg"

            val detectionDetailsFragment = DetailFragment.newInstance(detectionImage)


            val detectionDetailsFragmentManager = supportFragmentManager
            detectionDetailsFragmentManager.commit{
                replace(R.id.container, detectionDetailsFragment, DetailFragment::class.java.simpleName)
            }

            //toggleVisibility(false)

        }
    }

    fun toggleVisibility(show: Boolean){
        if (show) {
            binding.detectingScreen.visibility = View.VISIBLE
        } else {
            binding.detectingScreen.visibility = View.GONE
        }
    }
}