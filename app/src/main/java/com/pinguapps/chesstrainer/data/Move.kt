package com.pinguapps.chesstrainer.data


/** Move class
 *
 * @property shortNotation short form generic notation ie e2e4, g1f3. this is the notation used by most engines
 * @property longNotation classical human chess notation ie nf6, e4,O-O-O
 * @property enPassantSquare if move is en passant, square the capturing pawn ends on
 * @property castling if move is castling move, and which type
 *
 */
class Move(
    val endSquare: Square,
    val startSquare: Square,
    val pieceType: PieceType,
    val isCapture: Boolean = false,
    var shortNotation: String = "",
    val longNotation: String = "",
    val castling: Castleing = Castleing.NONE,
    val enPassantSquare: Square? = null,
) {
    init {
        generateShortNotation()
        //todo generate human notation
    }

    private fun generateShortNotation(){
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
        shortNotation = notation
    }

}