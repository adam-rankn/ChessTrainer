package com.pinguapps.chesstrainer.data

class Move(
    val endSquare: Square,
    val startSquare: Square,
    val piece: PieceType,
    val isCapture: Boolean = false,
    val notation: String = "",
    val castling: Castleing = Castleing.NONE,
    val enPassantSquare: Square? = null,
) {

    //todo notation

    init {
        val notation = ""
    }

}