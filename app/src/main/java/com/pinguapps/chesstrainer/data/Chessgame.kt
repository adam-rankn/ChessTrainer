package com.pinguapps.chesstrainer.data

import java.util.Stack

class Chessgame(color: Color= Color.WHITE) {

    var chessboard: Chessboard = Chessboard()

    val playerColor = color
    val toMove = Color.WHITE
    val moveHistory: Stack<Move> = Stack<Move>()
    var selectedSquare : Square? = null
    var validMoves = mutableListOf<Move>()


    init {
        chessboard.playerColor = playerColor
    }
    fun loadPositionFenString(fenString: String){
        chessboard.loadPositionFenString(fenString)
    }

    /**
     * sends the move to the chessboard and adds move to the move stack to facilitate undo move
     *
     */
    fun makeMove(square: Square){
        for (move in chessboard.validMoves) {
            if (move.endSquare == square) {
                chessboard.makeMove(move)
                moveHistory.push(move)
            }
        }
    }
}