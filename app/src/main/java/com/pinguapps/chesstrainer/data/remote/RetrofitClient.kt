package com.pinguapps.chesstrainer.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val TIME_OUT: Long = 120
    private const val BASE_URL = "https://explorer.lichess.ovh/"

    private val gson = GsonBuilder().setLenient().create()

    val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(RequestInterceptor)
        .build()

    fun getClient(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

/*
    val retrofit: RetrofitInterface by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Api.BASE_URL)
            .client(okHttpClient)
            .build().create(RetrofitInterface::class.java)
    }*/

}