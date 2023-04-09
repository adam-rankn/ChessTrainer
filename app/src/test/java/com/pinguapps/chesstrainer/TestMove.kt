package com.pinguapps.chesstrainer

import com.pinguapps.chesstrainer.data.Chessboard
import com.pinguapps.chesstrainer.data.Move
import com.pinguapps.chesstrainer.data.PieceType
import com.pinguapps.chesstrainer.logic.Chessgame
import org.junit.Test
import org.junit.Assert.*

class TestMove {

    @Test
    fun testMoveNotation(){
        val game = Chessgame()
        val board = game.chessboard
        val knightMove = Move(
            board.getSquare("f3"), board.getSquare("g1")
            , PieceType.KNIGHT, PieceType.NONE
        )
        assertEquals("g1f3",knightMove.uciNotation)

        val e4Move = Move(
            board.getSquare("e4"), board.getSquare("e2")
            , PieceType.PAWN, PieceType.NONE
        )
        assertEquals("e2e4",e4Move.uciNotation)

        val castlesMove = Move(
            endSquare = board.getSquare("g1"),
            startSquare = board.getSquare("e1"),
            pieceType = PieceType.KING
        )

        assertEquals("e1g1",castlesMove.uciNotation)

    }

}