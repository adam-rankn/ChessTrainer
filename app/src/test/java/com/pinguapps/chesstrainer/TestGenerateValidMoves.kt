package com.pinguapps.chesstrainer

import com.pinguapps.chesstrainer.data.Chessboard
import com.pinguapps.chesstrainer.logic.generateValidKnightMoves
import org.junit.Assert
import org.junit.Test

class TestGenerateValidMoves {

    @Test
    fun testGenerateValidKnightMoves() {
        val board = Chessboard()
        val moves = generateValidKnightMoves(board,board.getSquare("g1"))
        Assert.assertEquals(2, moves.size)

        board.loadPositionFenString("rnbqkbnr/pppppppp/8/8/3N4/8/PP1P1PPP/RNBQKB1R w KQkq - 0 1")
        val moves2 = board.generateValidKnightMoves(board.getSquare("d4"))
        Assert.assertEquals(8, moves2.size)

        //knights cannot move if pinned
        board.loadPositionFenString("rnbqk1nr/pppp1ppp/4p3/8/1b6/2NP4/PPP1PPPP/R1BQKBNR w KQkq - 0 1")
        val moves3 = board.generateValidKnightMoves(board.getSquare("c3"))
        Assert.assertEquals(0, moves3.size)
    }
}