package com.hse.education.data.network.apiService

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceProvider {

    const val BASE_URL = "http://10.0.2.2:8080"


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiServiceUser: ApiServiceUser by lazy {
        retrofit.create(ApiServiceUser::class.java)
    }

}