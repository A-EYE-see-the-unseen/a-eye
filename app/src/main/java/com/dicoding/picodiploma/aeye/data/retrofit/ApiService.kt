package com.dicoding.picodiploma.aeye.data.retrofit

import com.dicoding.picodiploma.aeye.data.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("logout")
    fun logout(
        @Header("Authorization") token: String
    ): Call<LogoutResponse>

    @FormUrlEncoded
    @POST("store-report")
    fun storeReport(
        @Header("Authorization") token: String,
        @Field("foto") foto: String,
        @Field("keterangan") keterangan: String,
    ): Call<ReportResponse>

    @POST("start-instance")
    fun startInstance(): Call<InstanceResponse>

    @POST("stop-instance")
    fun stopInstance(): Call<InstanceResponse>

    @GET("get-report")
    fun getReport(
        @Header("Authorization") token: String
    ): Call<ReportResponse>
}