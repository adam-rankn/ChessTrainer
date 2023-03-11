package com.pinguapps.chesstrainer.data

import java.util.Stack

class Chessgame(color: Color= Color.WHITE) {

    var chessboard: Chessboard = Chessboard()

    val playerColor = color
    val toMove = Color.WHITE
    val moveHistory: Stack<Move> = Stack<Move>()
    //todo add moves to stack
    var selectedSquare : Square? = null
    var validMoves = mutableListOf<Move>()

    var whiteCastleQueenRights = true
    var whiteCastleKingRights  = true
    var blackCastleQueenRights = true
    var blackCastleKingRights  = true


    init {
        chessboard.playerColor = playerColor
    }
    fun loadPositionFenString(fenString: String){
        chessboard.loadPositionFenString(fenString)
    }

    fun makeMove(square: Square){
        chessboard.makeMove(square)
        //TODO generate notation and add to stack
    }
}