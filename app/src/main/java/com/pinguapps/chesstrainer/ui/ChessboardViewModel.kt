package com.pinguapps.chesstrainer.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinguapps.chesstrainer.data.Chessboard

class ChessboardViewModel: ViewModel() {

    //val chessboardFlow = MutableStateFlow(Chessboard())
    val chessboard = MutableLiveData(Chessboard())



}