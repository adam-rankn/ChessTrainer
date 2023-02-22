package com.pinguapps.chesstrainer.data

interface Piece {

    val color: Color
    val type: PieceType

    fun isMoveValid(): Boolean {

        return true
    }
}