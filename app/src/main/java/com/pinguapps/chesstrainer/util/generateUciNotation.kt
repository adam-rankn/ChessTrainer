package com.pinguapps.chesstrainer.util

import android.util.Log
import com.pinguapps.chesstrainer.data.PieceType
import com.pinguapps.chesstrainer.data.Square

fun generateUciNotation(startSquare: Square, endSquare: Square): String {
    //todo promotion, castles
    var notation = ""
    val colMap = hashMapOf(
        0 to "a",
        1 to "b",
        2 to "c",
        3 to "d",
        4 to "e",
        5 to "f",
        6 to "g",
        7 to "h",
    )
    notation += colMap[startSquare.col]
    notation += 7 - (startSquare.row) + 1
    notation += colMap[endSquare.col]
    notation += 7 - (endSquare.row) + 1

    when (notation){
        "e1c1" -> notation = "e1c1c"
        "e1g1" -> notation = "e1g1c"
        "e8c8" -> notation = "e8c8c"
        "e8g8" -> notation = "e8g8c"
    }

    return notation
}