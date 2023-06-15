package com.dicoding.picodiploma.aeye.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.aeye.ui.detecting.DetectingActivity
import com.dicoding.picodiploma.aeye.ui.detecting.SocketHandler
import com.dicoding.picodiploma.loginactivity.R
import com.dicoding.picodiploma.loginactivity.databinding.ActivityDashboardBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.socket.emitter.Emitter

class DetailFragment : Fragment() {

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

    fun destroyFragment() {
        // Obtain a reference to the FragmentManager
        val fragmentManager = parentFragmentManager

        // Remove this fragment from its container
        fragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSubmit = view.findViewById(R.id.btnSubmit)
        btnReject = view.findViewById(R.id.btnReject)
        detectionImage = view.findViewById(R.id.detection_image)
        catatanPengawas = view.findViewById(R.id.somethingEditText)

        Log.e("Image Link", ARG_IMG_LINK)

        Glide.with(view.context)
            .load(ARG_IMG_LINK)
            .into(detectionImage)


        btnSubmit.setOnClickListener{
            val note = catatanPengawas.text

            mSocket.emit("detection-confirm", note)

            var toastShown = false

            var finalMessage = "No Changes :("
            mSocket.on("confirmation-response", object : Emitter.Listener {
                override fun call(vararg args: Any?) {
                    val message = args[0] as String
                    Log.e("Message received", message)
                    finalMessage = message
                    activity?.runOnUiThread{
                        Toast.makeText(view.context, finalMessage, Toast.LENGTH_SHORT).show()
                        toastShown = true
                    }
                    if (toastShown){
                        destroyFragment()
                    }
                }
            })

        }

        btnReject.setOnClickListener {

            var toastShown = false

            var message = "No Changes :("
            mSocket.emit("detection-reject")
            mSocket.on("confirmation-response", object : Emitter.Listener {
                override fun call(vararg args: Any?) {
                    message = args[0] as String
                    activity?.runOnUiThread{
                        Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
                        toastShown = true
                    }
                    if (toastShown){
//                        destroyFragment()       //THIS DOESN'T WORK UPON FIRST CLICK, AND CRASHES UPON SECOND FRAGMENT CLICK
//                        parentFragmentManager.beginTransaction().apply {
//                            addToBackStack(null)
//                            commit()
//                        }
                        onDestroyView()
                    }

                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

}