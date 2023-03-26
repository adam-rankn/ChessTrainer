package com.pinguapps.chesstrainer.logic

import com.pinguapps.chesstrainer.data.Chessgame
import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.puzzles.KnightMovePuzzles
import kotlin.random.Random

class KnightMovePuzzle(
    val minDifficulty: Int = 6,
    val maxDifficulty: Int = 7
) {

    val game = Chessgame()
    val chessboard = game.chessboard
    var player: Color = Color.WHITE
    var opponent: Color = Color.BLACK
    private val randomGenerator = Random(System.currentTimeMillis())
    val puzzle = pickKnightPuzzle()


    init {

        //game.loadPositionFenString(puzzle.first)
    }
    fun pickKnightPuzzle(): Triple<String, List<String>, Int> {
        val puzzleMainList = KnightMovePuzzles()
        val allPuzzles = mutableListOf<Triple<String,List<String>,Int>>()
        if (3 in minDifficulty..maxDifficulty) {
            allPuzzles.addAll(puzzleMainList.knightMoveList3Ply)
        }
        if (4 in minDifficulty..maxDifficulty) {
            allPuzzles.addAll(puzzleMainList.knightMoveList4Ply)
        }
        if (5 in minDifficulty..maxDifficulty) {
            allPuzzles.addAll(puzzleMainList.knightMoveList5Ply)
        }
        if (6 in minDifficulty..maxDifficulty) {
            allPuzzles.addAll(puzzleMainList.knightMoveList6Ply)
        }

        val puzzle = allPuzzles[randomGenerator.nextInt(0,allPuzzles.size)]
        return puzzle
    }
}