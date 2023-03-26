package com.pinguapps.chesstrainer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pinguapps.chesstrainer.data.puzzles.KnightPuzzleGame
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class TestKnightPuzzleGame {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun testResetBoard(){
        val knightGame = KnightPuzzleGame()
        val boardFen = knightGame.generateFenStringFromPosition()
        knightGame.restartGame()
        assertEquals(boardFen,knightGame.generateFenStringFromPosition())
    }
}