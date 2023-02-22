package com.pinguapps.chesstrainer.data

import java.util.Stack

class Chessgame {

    val board: Chessboard = Chessboard()

    val moveHistory: Stack<Move> = Stack<Move>()
}