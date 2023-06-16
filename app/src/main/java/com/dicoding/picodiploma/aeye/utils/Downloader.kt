package com.dicoding.picodiploma.aeye.utils

interface Downloader {
    fun downloadFile(url: String): Long
}