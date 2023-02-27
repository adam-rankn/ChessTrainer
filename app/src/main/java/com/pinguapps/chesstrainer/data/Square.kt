package com.pinguapps.chesstrainer.data

class Square(val row: Int, val col: Int) {

    var piece: Piece = Piece(Color.BLACK,PieceType.NONE)
    val color: Color = if (row + col % 2 == 0) {
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


}