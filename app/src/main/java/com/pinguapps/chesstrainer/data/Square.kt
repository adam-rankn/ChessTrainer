package com.pinguapps.chesstrainer.data

class Square(val row: Int, val col: Int) {

    val piece: Piece? = null
    val color: Color = if (row + col % 2 == 0) {
        Color.BLACK }
    else {
        Color.WHITE
    }
}