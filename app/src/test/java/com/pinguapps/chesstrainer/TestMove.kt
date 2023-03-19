package com.pinguapps.chesstrainer

import com.pinguapps.chesstrainer.data.Chessboard
import com.pinguapps.chesstrainer.data.Move
import com.pinguapps.chesstrainer.data.PieceType
import org.junit.Test
import org.junit.Assert.*

class TestMove {

    @Test
    fun testMoveNotation(){
        val board = Chessboard()
        val knightMove = Move(board.getSquare("f3"),board.getSquare("g1")
            ,PieceType.KNIGHT,PieceType.NONE)
        assertEquals("g1f3",knightMove.shortNotation)

        val e4Move = Move(board.getSquare("e4"),board.getSquare("e2")
            ,PieceType.PAWN,PieceType.NONE)
        assertEquals("e2e4",e4Move.shortNotation)
    }
}