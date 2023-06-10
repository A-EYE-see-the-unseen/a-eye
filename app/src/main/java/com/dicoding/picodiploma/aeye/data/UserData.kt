package com.dicoding.picodiploma.aeye.data

import com.google.gson.annotations.SerializedName

data class UserData(

    @field:SerializedName("nip")
    val nip: String?,

    @field:SerializedName("nama_pengawas")
    val nama_pengawas: String?,

)
