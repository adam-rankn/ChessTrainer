package com.pinguapps.chesstrainer.data

import com.pinguapps.chesstrainer.util.generateUciNotation


/** Move class
 *
 * @property uciNotation short form generic notation ie e2e4, g1f3. this is the notation used by most engines
 * @property longNotation classical human chess notation ie nf6, e4,O-O-O
 * @property enPassantSquare if move is en passant, square the capturing pawn ends on
 * @property castling if move is castling move, and which type
 *
 */
class Move(
    val endSquare: Square,
    val startSquare: Square,
    val pieceType: PieceType,
    val capturedPiece: PieceType = PieceType.NONE,
    var uciNotation: String = "",
    val longNotation: String = "",
    val castling: Castleing = Castleing.NONE,
    val enPassantSquare: Square? = null
) {
    init {
        uciNotation = generateUciNotation(startSquare = startSquare, endSquare = endSquare)
        //todo generate human notation SAN etc
    }


}