package com.pinguapps.chesstrainer.logic

import com.pinguapps.chesstrainer.data.puzzles.KnightMovePuzzles
import kotlin.random.Random

class KnightMovePuzzleGenerator(
    val minDifficulty: Int = 3,
    val maxDifficulty: Int = 7
) {

    private val randomGenerator = Random(System.currentTimeMillis())
    val puzzle = pickKnightPuzzle()

    fun pickKnightPuzzle(): Triple<String, List<String>, Int> {
        val puzzleMainList = KnightMovePuzzles()
        val allPuzzles = mutableListOf<Triple<String, List<String>, Int>>()
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
        return allPuzzles[randomGenerator.nextInt(0, allPuzzles.size)]
    }
}