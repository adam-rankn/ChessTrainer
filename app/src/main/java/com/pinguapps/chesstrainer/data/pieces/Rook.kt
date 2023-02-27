package com.pinguapps.chesstrainer.data.pieces

import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.Move
import com.pinguapps.chesstrainer.data.Piece
import com.pinguapps.chesstrainer.data.Square

class Rook(val color: Color,val square: Square) {


/*    fun isMoveValid(move: Move): Boolean {
        val target = move.endSquare

        // own piece in square, cannot move
        if (target.piece != null && target.piece.color == color) {
            return false
        }
        //cannot move to current square
        else if (target == square) {
            return false
        }
        else if (move.endSquare.col == square.col) {
            for (i in move.endSquare.col .. square.col){
                //todo
                return false
            }
        }
        return false
    }*/

}