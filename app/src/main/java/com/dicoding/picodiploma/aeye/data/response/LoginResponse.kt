package com.dicoding.picodiploma.aeye.data.response

import com.dicoding.picodiploma.aeye.data.UserData

data class LoginResponse (
    val token: String,
    val userResult: UserData
)
