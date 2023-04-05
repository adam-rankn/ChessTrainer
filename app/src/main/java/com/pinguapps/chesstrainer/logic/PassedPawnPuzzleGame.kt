package com.pinguapps.chesstrainer.logic

import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.GameResult
import com.pinguapps.chesstrainer.data.PieceType
import com.pinguapps.chesstrainer.data.Square

class PassedPawnPuzzleGame(
    var puzzle: String = PassedPawnPuzzleGenerator().generateRandomPawnPuzzle()
): Chessgame() {


    init {
        chessboard.loadPositionFenString(puzzle)
    }

    override fun makeHumanMove(square: Square) {
        super.makeHumanMove(square)
        // if move promotes, game over
       if (!((square.row == 0 || square.row == 7) && (square.pieceType == PieceType.PAWN))){
            makeComputerMove()
        }
    }

    /**
     * override normal behavior, promote pawn to queen automatically
     */
    override fun promotePawn(square: Square, pieceType: PieceType, color: Color){
        super.promotePawn(square, PieceType.QUEEN, color)
    }

    /**
     * win the game when a pawn reaches 8th rank
     */
    override fun setPawnPromoted(square: Square){
        if (square.row == 0 && !canKingTake(square)) {
            gameResult.value = GameResult.PUZZLE_WON
            promotePawn(square, PieceType.QUEEN, Color.WHITE)
        }
        else if (gameResult.value == GameResult.GAME_IN_PROGRESS){
            gameResult.value = GameResult.PUZZLE_FAILED
            promotePawn(square, PieceType.QUEEN,Color.BLACK)
        }
    }

    /**
     * generates a new puzzle
     */
    override fun newGame() {
        super.newGame()
        puzzle = PassedPawnPuzzleGenerator().generateRandomPawnPuzzle()
        chessboard.loadPositionFenString(puzzle)
    }

    /**
     * resets the board with the same puzzle
     */
    override fun restartGame(){
        super.newGame()
        loadPositionFenString(puzzle)

    }

    fun canKingTake(square: Square): Boolean {
        val row = square.row
        val col = square.col

        if (col != 0 && chessboard.board[col-1][row].pieceType == PieceType.KING
            && chessboard.board[col-1][row].pieceColor != playerColor){
            return true
        }
        if (col != 0 && chessboard.board[col-1][row+1].pieceType == PieceType.KING
            && chessboard.board[col-1][row].pieceColor != playerColor){
            return true
        }
        if (chessboard.board[col][row+1].pieceType == PieceType.KING
            && chessboard.board[col-1][row].pieceColor != playerColor){
            return true
        }
        if (col != 7 && chessboard.board[col+1][row+1].pieceType == PieceType.KING
            && chessboard.board[col-1][row].pieceColor != playerColor){
            return true
        }
        if (col != 7 && chessboard.board[col+1][row].pieceType == PieceType.KING
            && chessboard.board[col-1][row].pieceColor != playerColor){
            return true
        }
        return false
    }
}