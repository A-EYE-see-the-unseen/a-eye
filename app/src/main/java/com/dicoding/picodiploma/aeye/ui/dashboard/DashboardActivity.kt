package com.dicoding.picodiploma.aeye.ui.dashboard

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dicoding.picodiploma.aeye.data.response.InstanceResponse
import com.dicoding.picodiploma.aeye.data.retrofit.ApiConfig
import com.dicoding.picodiploma.aeye.data.storage.SharedPref
import com.dicoding.picodiploma.aeye.ui.detecting.DetectingActivity
import com.dicoding.picodiploma.aeye.ui.login.LoginActivity
import com.dicoding.picodiploma.loginactivity.databinding.ActivityDashboardBinding
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDashboardBinding
    lateinit var sharedPref: SharedPref

    //VARS
    private val STORAGE_CODE = 1001
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

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

    }

    override fun onClick(v: View) {
        when (v.id) {
            com.dicoding.picodiploma.loginactivity.R.id.btnStartDetecting -> {
                binding.progressBar.visibility = View.VISIBLE
                startInstance()
            }
            com.dicoding.picodiploma.loginactivity.R.id.btnReportPDF -> {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                    )
                    requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_CODE
                    )
                } else {
                    savePDF()
                }
            }
            com.dicoding.picodiploma.loginactivity.R.id.btnLogout -> {
                sharedPref.clearToken()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun savePDF() {
//        data class Item(val text: String, val imageUrl: String)
        val mDoc = Document()
        val mFileName = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())     //this can also be our time and date

        val mFilePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/" + mFileName + ".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addAuthor("CRLF's copy and pasted work")

            val data = "this new pdf yeyyyy. also uh, was created on ${mFileName}"
            mDoc.add(Paragraph(data))
            mDoc.close()
            Toast.makeText(
                this,
                "${mFileName} file successfully created in \n${mFilePath}",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )                //this line doesn't exist in the tutorial
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePDF()
                } else {
                    Toast.makeText(
                        this,
                        "bruh we did not get storage permission :(",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<InstanceResponse>, t: Throwable) {
                    // Handle network failures or exceptions
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Cek koneksi internet anda.", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }
}