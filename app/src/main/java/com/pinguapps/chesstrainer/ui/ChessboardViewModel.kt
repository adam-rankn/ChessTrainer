package com.pinguapps.chesstrainer.ui

import androidx.lifecycle.ViewModel
import com.pinguapps.chesstrainer.logic.Chessgame

class ChessboardViewModel: ViewModel() {

    //val chessboardFlow = MutableStateFlow(Chessboard())
    val chessgame = Chessgame()

    fun undoMove(){
        chessgame.undoMove()
    }

    fun redoMove(){
        chessgame.redoMove()
    }

    fun undoAllMoves(){
        chessgame.undoAllMoves()
    }

    fun redoAllMoves(){
        chessgame.redoAllMoves()
    }

    fun resetGame(){
        chessgame.newGame()
    }




}