package com.dicoding.picodiploma.aeye.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.aeye.ui.detecting.DetectingActivity
import com.dicoding.picodiploma.aeye.ui.detecting.SocketHandler
import com.dicoding.picodiploma.loginactivity.databinding.ActivityDetectedBinding
import io.socket.emitter.Emitter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class DetectedActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetectedBinding

    private lateinit var detectionImage: ImageView
    private lateinit var btnSubmit: AppCompatButton
    private lateinit var btnReject: AppCompatButton
    private lateinit var catatanPengawas: EditText

    val mSocket = SocketHandler.getSocket()

    companion object{
        private var ARG_IMG_LINK = "arg_img_link"
        fun newInstance(imageLink: String) : DetailFragment{
            val thisFragment = DetailFragment()

            ARG_IMG_LINK = imageLink
            //Log.e("userdata companion", "USERNAME IS: ${user_object.login}")
            return thisFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnSubmit = binding.btnSubmit
        btnReject = binding.btnReject
        detectionImage = binding.detectionImage
        catatanPengawas = binding.somethingEditText

        Log.e("Image Link", ARG_IMG_LINK)
        Glide.with(this)
            .load(ARG_IMG_LINK)
            .into(detectionImage)


        btnSubmit.setOnClickListener{//TODO ISI FUNGSI API
            toggleVisibility(true)

            val note = catatanPengawas.text


            runOnUiThread {
                    Toast.makeText(applicationContext, "Submitted", Toast.LENGTH_SHORT).show()

                    runBlocking {
                        delay(2000)                                                         //saia tidak suka solusi ini tapi yah mau diapa lagi :v
                        val intent = Intent(applicationContext, DetectingActivity::class.java)
                        startActivity(intent)
                    }
            }


        }

        btnReject.setOnClickListener {  //TODO ISI FUNGSI API
            toggleVisibility(true)
            runOnUiThread {
                Toast.makeText(applicationContext, "Rejected.", Toast.LENGTH_SHORT).show()

                runBlocking {
                    delay(2000)                                                         //saia tidak suka solusi ini tapi yah mau diapa lagi :v
                    val intent = Intent(applicationContext, DetectingActivity::class.java)
                    startActivity(intent)
                }
            }

        }


    }

    fun toggleVisibility(isClicked: Boolean){
        if (isClicked) {
            binding.btnSubmit.visibility = View.GONE
            binding.btnReject.visibility = View.GONE
        }
    }
}