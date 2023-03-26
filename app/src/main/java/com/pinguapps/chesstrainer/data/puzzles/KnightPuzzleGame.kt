package com.pinguapps.chesstrainer.data.puzzles

import android.util.Log
import com.pinguapps.chesstrainer.data.Chessgame
import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.GameResult
import com.pinguapps.chesstrainer.data.Square
import com.pinguapps.chesstrainer.logic.KnightMovePuzzle
import kotlin.math.abs

class KnightPuzzleGame(
    var puzzle: Triple<String, List<String>, Int> = KnightMovePuzzle().puzzle,
    //todo difficulty slider

    ): Chessgame() {


    private fun getSquareString(square: Square): String {
        val col = when (square.col){
            0 -> "a"
            1 -> "b"
            2 -> "c"
            3 ->"d"
            4 ->"e"
            5 ->"f"
            6 ->"g"
            7 ->"h"

            else -> ""}
        val row = (abs(square.row-7)+1).toString()
        return "$col$row"
    }
    init {
        chessboard.loadPositionFenString(puzzle.first)
        targetSquare = chessboard.getSquare(puzzle.second.last())
    }

    /**
     * checks the special win/loss conditions for this type of puzzle. If incorrect move has been
     * played, set fail , if target square reached, set win
     */
    override fun makeHumanMove(square: Square){
        super.makeHumanMove(square)
        if (getSquareString(square) !in puzzle.second){
            Log.d("knightpuzzle", getSquareString(square))
            Log.d("knightpuzzle", puzzle.second.toString())
            gameResult.postValue(GameResult.PUZZLE_FAILED)
        }
        else if (getSquareString(square) == puzzle.second.last()){
            gameResult.postValue(GameResult.PUZZLE_WON)
        }
        chessboard.turn = Color.WHITE
    }

    /**
     * override the computer move function as this type of game is single player
     */
    override fun makeComputerMove(){
    }

    /**
     * generates a new puzzle
     */
    override fun newGame() {
        super.newGame()
        puzzle = KnightMovePuzzle().puzzle
        chessboard.loadPositionFenString(puzzle.first)
        targetSquare = chessboard.getSquare(puzzle.second.last())
    }

    /**
     * resets the board with the same puzzle
     */
    override fun restartGame(){
        super.newGame()
        loadPositionFenString(puzzle.first)

    }







}