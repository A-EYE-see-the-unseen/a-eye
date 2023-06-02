package com.dicoding.picodiploma.aeye.data.retrofit

import com.dicoding.picodiploma.aeye.data.response.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}