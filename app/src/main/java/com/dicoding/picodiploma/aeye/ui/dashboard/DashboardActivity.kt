package com.dicoding.picodiploma.aeye.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dicoding.picodiploma.loginactivity.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

    }

    override fun onClick(v: View?) {

    }
}