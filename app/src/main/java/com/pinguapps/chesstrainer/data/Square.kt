package com.pinguapps.chesstrainer.data

class Square(val row: Int, val col: Int) {

    var piece: Piece = Piece(Color.BLACK,PieceType.NONE)
    val squareColor: Color = if (row + col % 2 == 0) {
        Color.BLACK }
    else {
        Color.WHITE
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Square){
            this.col == other.col && this.row == other.row
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        result = 31 * result + piece.hashCode()
        result = 31 * result + squareColor.hashCode()
        return result
    }


}