package com.pinguapps.chesstrainer.data

class Move(
    val endSquare: Square,
    val startSquare: Square,
    val piece: PieceType,
    val isCapture: Boolean = false,
    val notation: String = ""
) {

    //todo

}