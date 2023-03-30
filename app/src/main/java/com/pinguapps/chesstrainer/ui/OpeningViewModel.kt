package com.pinguapps.chesstrainer.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinguapps.chesstrainer.data.remote.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class OpeningViewModel: ViewModel() {
    private val lichessRepository = LichessApiRepository()

    fun getMoves(fen: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val moves = lichessRepository.getMovesFromFen(fen)
            Log.d("lichess moves", moves.toString())
        }
    }
}