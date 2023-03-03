package com.pinguapps.chesstrainer.data

import java.util.Stack

class Chessgame {

    val board: Chessboard = Chessboard()

    val moveHistory: Stack<Move> = Stack<Move>()
    var selectedSquare : Square? = null
    var validMoves = mutableListOf<Move>()
}