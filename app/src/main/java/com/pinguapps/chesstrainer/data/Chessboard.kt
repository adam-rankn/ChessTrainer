package com.pinguapps.chesstrainer.data

class Chessboard() {

    private val board1 = Array(8) { Array(8) { Square(0,0) } }

    var board4 =  Array(8) { col -> Array(8) { Square(0,col) } }
    var board2 =  { row: Int -> Array(8) { col -> Array(8) { Square(row,col) } }}

    var board3 = Array(8) { Array<Square>(8) {Square(0,0)} }

    val board = Array(8) { row -> Array(8) { col -> Square(row,col) } }



    init {
        board[0][0]
    }

    fun getSquare(notation: String): Square {
        val rowStr: String = notation.substring(0,1)
        val colStr: String = notation.substring(1,2)

        val col = colStr.toInt() - 1
        val row = when (rowStr){
            "a" -> 0
            "b" -> 1
            "c" -> 2
            "d" -> 3
            "e" -> 4
            "f" -> 5
            "g" -> 6
            "h" -> 7

            else -> 0
        }

        return (board[row][col])
    }
}

