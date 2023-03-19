package com.pinguapps.chesstrainer.data

import androidx.lifecycle.MutableLiveData
import java.util.Stack

class Chessgame(color: Color= Color.WHITE) {

    var chessboard: Chessboard = Chessboard()

    val playerColor = color
    val toMove = Color.WHITE
    val moveHistory: Stack<String> = Stack()
    val futureMoves: Stack<String> = Stack()
    val gameResult = MutableLiveData(GameResult.GAME_IN_PROGRESS)
    var hintsRemaining = 0
    var fiftyMoveCounter = 0
    var moveCounter = 1


    init {
        chessboard.playerColor = playerColor
    }
    fun loadPositionFenString(fenString: String){
        val fenList = fenString.split(" ")
        val halfMoveString = fenList[4]
        val fullMoveString = fenList[5]
        chessboard.loadPositionFenString(fenString)
        fiftyMoveCounter = halfMoveString.toInt()
        moveCounter = fullMoveString.toInt()
    }

    fun generateFenStringFromPosition(): String {
        var fen = chessboard.getPartialFenStringFromPosition()
        fen += fiftyMoveCounter.toString()
        fen +=" "
        fen += moveCounter.toString()
        return fen
    }

    /**
     * sends the move to the chessboard and adds move to the move stack to facilitate undo move
     * empties future move history if move is played with moves on the future stack
     *
     */
    fun makeMove(square: Square){
        for (move in chessboard.validMoves) {
            if (move.endSquare == square) {
                moveHistory.push(generateFenStringFromPosition())
                if (chessboard.turn == Color.BLACK){
                    moveCounter ++
                }
                chessboard.selectedSquare = move.startSquare // needed in case of speech to text
                chessboard.makeMove(move)
                futureMoves.clear()
                updateFiftyMoveCounter(move)
            }
        }
    }

    /**
     * puts current bord position on the future move stack and loads previous position
     * @see redoMove
     *
     */
    fun undoMove() {
        if (moveHistory.isEmpty()){
            return
        }
        if (moveHistory.isNotEmpty()) {
            val currentBoardPosition = generateFenStringFromPosition()
            futureMoves.push(currentBoardPosition)
            val lastMoveFenString = moveHistory.pop()
            loadPositionFenString(lastMoveFenString)
        }
    }

    /**
     * puts current bord position on the past move stack and loads position from top of future move stack
     * @see undoMove
     *
     */
    fun redoMove() {
        if (futureMoves.isEmpty()){
            return
        }
        if (futureMoves.isNotEmpty()) {
            val currentBoardPosition = generateFenStringFromPosition()
            moveHistory.push(currentBoardPosition)
            loadPositionFenString(futureMoves.pop())
        }
    }

    /**
     * undoes all moves until start position is reached, adding each to the future move stack
     * also adds current position to the stack
     * @see undoMove
     *
     */
    fun undoAllMoves(){
        if (moveHistory.isEmpty()){
            return
        }
        moveHistory.push(generateFenStringFromPosition())
        while (moveHistory.isNotEmpty()){
            futureMoves.push(moveHistory.pop())
        }
        loadPositionFenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    }

    /**
     * redoes all moves in the future stack until position after most recent move is reached,
     * adding each to move history stack
     * @see redoMove
     *
     */
    fun redoAllMoves(){
        if (futureMoves.isEmpty()){
            return
        }
        while (futureMoves.isNotEmpty()){
            moveHistory.push(futureMoves.pop())
        }
        if (moveHistory.isNotEmpty()){
            loadPositionFenString(moveHistory.peek())
        }
    }

    /**
     * resigns the current game
     *
     */
    fun resign(){
        if (playerColor == Color.BLACK) {
            chessboard.result.postValue(GameResult.WHITE_WIN_RESIGNATION)
        }
        else if (playerColor == Color.WHITE){
            chessboard.result.postValue(GameResult.BLACK_WIN_RESIGNATION)
        }
    }

    /**
     * Increments or resets the fifty move counter and sets a draw if 50 moves ( 100 half moves) is reached
     */
    private fun updateFiftyMoveCounter(move: Move){
        if (move.capturedPiece != PieceType.NONE || move.pieceType == PieceType.PAWN){
            fiftyMoveCounter = 0
        }
        else {
            fiftyMoveCounter ++
        }
        if (fiftyMoveCounter == 100){
            gameResult.postValue(GameResult.DRAW_BY_FIFTY)
        }
    }



}