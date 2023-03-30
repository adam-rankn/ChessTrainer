package com.pinguapps.chesstrainer.logic

import com.pinguapps.chesstrainer.data.Chessboard
import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.puzzles.PassedPawnPuzzles
import kotlin.random.Random

class PassedPawnPuzzleGenerator {

    val chessboard = Chessboard()
    var player: Color = Color.WHITE
    var opponent: Color = Color.BLACK
    private val randomGenerator = Random(System.currentTimeMillis())
    val puzzles = PassedPawnPuzzles()


    init {
        chessboard.clearBoard()
        if ((0..1).random() < 1){
            player = Color.WHITE
            opponent = Color.BLACK
        }
        else {
            player = Color.BLACK
            opponent = Color.WHITE
        }
    }


    fun generateTypeWeightedRandomPawnPuzzle(): String {
        return  when (randomGenerator.nextInt(0,3)) {
            0 -> generateSquarePuzzle()
            1 -> generateSquarePuzzle()
            2 -> generateHatPuzzle()
            else -> {""}
        }
    }

    fun generateRandomPawnPuzzle(): String {
        return puzzles.allPuzzles[randomGenerator.nextInt(
            0,puzzles.allPuzzles.size)]
    }

    fun generateSquarePuzzle(): String {
        return puzzles.whiteSquarePositionsList[randomGenerator.nextInt(
            0, puzzles.whiteSquarePositionsList.size)]
    }

    fun generateHatPuzzle(): String {
        return puzzles.whiteHatPositionsList[randomGenerator.nextInt(
            0, puzzles.whiteHatPositionsList.size)]
    }

    fun generateSquigglyTrianglePuzzle(): String {
        return puzzles.whiteSquigglyPositionsList[randomGenerator.nextInt(
            0, puzzles.whiteSquigglyPositionsList.size)]
    }

    fun generateUndoublerPuzzle(): String {
        return puzzles.whiteUndoublerList[randomGenerator.nextInt(
            0, puzzles.whiteUndoublerList.size)]
    }
}