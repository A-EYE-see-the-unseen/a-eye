package com.dicoding.picodiploma.aeye.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
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


        btnSubmit.setOnClickListener{
            val note = catatanPengawas.text

            mSocket.emit("detection-confirm", note)

            var toastShown = false

            var finalMessage = "No Changes :("
            mSocket.on("confirmation-response") { args ->
                val message = args[0] as String
                Log.e("Message received", message)
                finalMessage = message
                runOnUiThread {
                    Toast.makeText(applicationContext, finalMessage, Toast.LENGTH_SHORT).show()
                    toastShown = true
                    runBlocking {
                        delay(2000)
                        val intent = Intent(applicationContext, DetectingActivity::class.java)
                        startActivity(intent)
                    }
                }
                if (toastShown) {
                    val intent = Intent(applicationContext, DetectingActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        btnReject.setOnClickListener {
            var message = "No Changes :("
            mSocket.emit("detection-reject")
            mSocket.on("confirmation-response", object : Emitter.Listener {
                override fun call(vararg args: Any?) {
                    message = args[0] as String
                    runOnUiThread{
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        runBlocking {
                            delay(2000)
                            val intent = Intent(applicationContext, DetectingActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            })
        }
    }
}