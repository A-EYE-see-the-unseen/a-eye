package com.dicoding.picodiploma.aeye.utils

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.dicoding.picodiploma.aeye.data.storage.SharedPref

class AndroidDownloader(private val context: Context): Downloader {
    lateinit var sharedPref: SharedPref
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        sharedPref = SharedPref(context)

        val request = DownloadManager.Request(url.toUri())
            .setMimeType("application/pdf")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("Laporan.pdf")
            .addRequestHeader("Authorization", "Bearer ${sharedPref.getToken().toString()}")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Laporan.pdf")
        return downloadManager.enqueue(request)
    }
}