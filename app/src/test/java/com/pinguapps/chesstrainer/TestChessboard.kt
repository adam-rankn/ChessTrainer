package com.pinguapps.chesstrainer

import com.pinguapps.chesstrainer.data.Chessboard
import com.pinguapps.chesstrainer.data.Color
import org.junit.Test

import org.junit.Assert.*

class TestChessboard {
    @Test
    fun testFindSquare() {
        val board = Chessboard()
        val squareA1 = board.getSquare("a1")
        assertEquals(0, squareA1.row)
        assertEquals(0, squareA1.col)

        val squareH8 = board.getSquare("h8")
        assertEquals(7, squareH8.row)
        assertEquals(7, squareH8.col)

        val squareA8 = board.getSquare("a8")
        assertEquals(0, squareA8.row)
        assertEquals(7, squareA8.col)
    }

    @Test
    fun testColor() {
        val board = Chessboard()
        val squareA1 = board.getSquare("a1")
        val squareA2 = board.getSquare("a2")
        val squareA8 = board.getSquare("a8")

        assertEquals(Color.BLACK,squareA1.color)
        assertEquals(Color.WHITE,squareA2.color)
        assertEquals(Color.WHITE,squareA8.color)
    }
}