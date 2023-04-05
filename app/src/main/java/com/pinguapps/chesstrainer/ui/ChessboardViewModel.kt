package com.pinguapps.chesstrainer.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pinguapps.chesstrainer.data.Square
import com.pinguapps.chesstrainer.logic.Chessgame

open class ChessboardViewModel: ViewModel() {

    val chessgame = Chessgame()

    /**
     * puts current board position on the future move stack and loads previous position
     * Also removes position from threefold repetition check map
     * @see redoMove
     *
     */
    fun undoMove(){
        chessgame.undoMove()
    }

    /**
     * puts current board position on the past move stack and loads position from top of future move stack
     * Also re-adds position to threefold repetition check map
     * @see undoMove
     *
     */
    fun redoMove(){
        chessgame.redoMove()
    }

    /**
     * undoes all moves until start position is reached, adding each to the future move stack
     * also adds current position to the stack. Also removes positions from threefold repetition check map
     * @see undoMove
     *
     */
    fun undoAllMoves(){
        chessgame.undoAllMoves()
        Log.d("viewmodel","undo all moves pressed")
    }

    /**
     * redoes all moves in the future stack until position after most recent move is reached,
     * adding each to move history stack. Also re-adds positions to threefold repetition check map
     * @see redoMove
     *
     */
    fun redoAllMoves(){
        chessgame.redoAllMoves()
    }

    fun resetGame(){
        chessgame.newGame()
    }
}