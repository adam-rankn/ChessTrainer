package com.pinguapps.chesstrainer.data.remote


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface LichessApi {

    @GET
    fun getMoves(@Url url: String): Call<LichessOpeningData>
}