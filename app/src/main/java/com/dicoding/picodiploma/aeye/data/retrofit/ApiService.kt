package com.dicoding.picodiploma.aeye.data.retrofit

import com.dicoding.picodiploma.aeye.data.response.InstanceResponse
import com.dicoding.picodiploma.aeye.data.response.LoginResponse
import com.dicoding.picodiploma.aeye.data.response.ReportResponse
import com.dicoding.picodiploma.aeye.data.response.VerifyResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("verify-token")
    fun verifyToken(
        @Header("Authorization") token: String
    ): Call<VerifyResponse>

    @POST("start-instance")
    fun startInstance(): Call<InstanceResponse>

    @POST("stop-instance")
    fun stopInstance(): Call<InstanceResponse>

    @GET("get-report")
    fun getReport(
        @Header("Authorization") token: String
    ): Call<ReportResponse>
}