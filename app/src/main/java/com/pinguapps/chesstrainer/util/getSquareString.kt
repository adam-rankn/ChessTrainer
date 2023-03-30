package com.pinguapps.chesstrainer.util

import com.pinguapps.chesstrainer.data.Square
import kotlin.math.abs

/**
 * gets the string notation for given square
 */
fun getSquareString(square: Square): String {
    val col = when (square.col){
        0 -> "a"
        1 -> "b"
        2 -> "c"
        3 ->"d"
        4 ->"e"
        5 ->"f"
        6 ->"g"
        7 ->"h"

        else -> ""}
    val row = (abs(square.row-7) +1).toString()
    return "$col$row"
}