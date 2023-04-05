package com.pinguapps.chesstrainer.data.remote

import android.util.Log
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


class LichessApiRepository {
    private val chessApi: LichessApi = RetrofitClient.getClient().create(LichessApi::class.java)


    /**
     * gets the data from lichess opening explorer api from the given position
     * @param url
     */
    fun getMovesFromFen(url: String): LichessResponse<LichessOpeningData> {
        val call: Call<LichessOpeningData> = chessApi.getMoves(url)
        return try {
            val response: Response<LichessOpeningData> = call.execute()
            LichessResponse.Success(response.body())

        } catch (e: IOException) {
            Log.d("lichess api error", "error: $e")
            LichessResponse.Failure(e)
        }
    }
}