package com.pinguapps.chesstrainer.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinguapps.chesstrainer.data.Chessboard
import com.pinguapps.chesstrainer.data.Chessgame

class ChessboardViewModel: ViewModel() {

    //val chessboardFlow = MutableStateFlow(Chessboard())
    val chessgame = Chessgame()



}