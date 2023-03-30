package com.pinguapps.chesstrainer.data.remote

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


class LichessApiRepository {
    private val chessApi: LichessApi = RetrofitClient.getClient().create(LichessApi::class.java)


    /**
     * gets the data from lichess opening explorer api from the given position
     * @param fen the FEN string to search for
     */
    fun getMovesFromFen(fen: String): LichessResponse<LichessOpeningdata> {
        val call: Call<LichessOpeningdata> = chessApi.getMoves(fen)
        return try {
            val response: Response<LichessOpeningdata> = call.execute()
            LichessResponse.Success(response.body())

        } catch (e: IOException) {
            Log.d("lichess api error", "error: $e")
            LichessResponse.Failure(e)
        }
    }
}