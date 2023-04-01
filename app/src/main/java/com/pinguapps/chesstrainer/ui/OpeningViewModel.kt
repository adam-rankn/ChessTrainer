package com.pinguapps.chesstrainer.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinguapps.chesstrainer.data.Opening
import com.pinguapps.chesstrainer.data.remote.*
import com.pinguapps.chesstrainer.logic.Chessgame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class OpeningViewModel: ViewModel() {
    private val lichessRepository = LichessApiRepository()
    val chessGame = Chessgame()

    fun getMoves(fen: String) {
        CoroutineScope(Dispatchers.Default).launch {
            val moves = lichessRepository.getMovesFromFen(fen)
            Log.d("lichess moves", moves.toString())
        }
    }

    private var _openingName: MutableStateFlow<String> = MutableStateFlow("")
    private var _predictions: MutableStateFlow<List<String>> = MutableStateFlow(mutableListOf())

    private var _showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)


    var opening = _openingName.asStateFlow()
    var showProgressBar = _showProgressBar.asStateFlow()


    fun getPredictions(query: String) {
        _openingName.value = query
    }

    fun onOpeningAutoCompleteClear() {
        viewModelScope.launch {
            _openingName.value = ""
            clearPredictions()
        }
    }

    fun onOpeningSelected(opening: Opening){
        chessGame.loadPositionFenString(opening.fen)
    }

    private fun clearPredictions() {
        _predictions.value = mutableListOf()
    }

}