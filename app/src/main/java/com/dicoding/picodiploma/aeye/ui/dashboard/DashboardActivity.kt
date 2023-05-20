package com.dicoding.picodiploma.aeye.ui.dashboard

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.dicoding.picodiploma.loginactivity.databinding.ActivityDashboardBinding
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityDashboardBinding

    //VARS
    private val STORAGE_CODE = 1001

    private val REQUEST_EXTERNAL_STORAGE = 1

    private val PERMISSIONS_STORAGE = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    // gtw cara misahin ;-;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        binding.btnReportPDF.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@DashboardActivity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                    )
                    val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, STORAGE_CODE)
                    savePDF()
                } else {
                    savePDF()
                }
            }

        }
    }

    override fun onClick(v: View?) {

    }

    private fun savePDF() {
        val mDoc = Document()
        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())     //this can also be our time and date


        val mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + mFileName + ".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()

            val data = "this new pdf yeyyyy. also uh, was created on ${mFileName}"
            mDoc.addAuthor("CRLF's copy and pasted work")
            mDoc.add(Paragraph(data))
            mDoc.close()
            Toast.makeText(this, "${mFileName} file successfully created in \n${mFilePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }

        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)                //this line doesn't exist in the tutorial
            when(requestCode){
                STORAGE_CODE -> {
                    if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        savePDF()
                    } else {
                        Toast.makeText(this, "bruh we did not get storage permission :(", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
}