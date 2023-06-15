package com.dicoding.picodiploma.aeye.data

import com.google.gson.annotations.SerializedName

data class UserData(
    var id_pengawas: Int?,
    val nip: String?,
    val nama_pengawas: String?,
    val email: String?
)
