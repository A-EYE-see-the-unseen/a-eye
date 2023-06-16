package com.dicoding.picodiploma.aeye.data.response

import com.dicoding.picodiploma.aeye.data.UserData

data class LoginResponse (
    val accessToken: String,
    val userResult: UserData
)
