package com.dicoding.picodiploma.aeye.data.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://a-eye-project.et.r.appspot.com/api/")
                .addConverterFactory(MoshiConverterFactory.create().asLenient())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}