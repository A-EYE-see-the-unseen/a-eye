package com.dicoding.picodiploma.aeye.ui.detecting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.aeye.ui.dashboard.DashboardActivity
import com.dicoding.picodiploma.loginactivity.databinding.ActivityDetectingBinding

class DetectingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetectingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        binding.btnBerhenti.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}