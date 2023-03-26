package com.pinguapps.chesstrainer.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pinguapps.chesstrainer.engine.cuckoo.chess.ComputerPlayer
import com.pinguapps.chesstrainer.logic.bots.BasicBot
import com.pinguapps.chesstrainer.logic.bots.Bot
import com.pinguapps.chesstrainer.util.fenToEnginePos
import java.util.*


open class Chessgame(color: Color= Color.WHITE, bot: Bot = BasicBot()) {

    val chessboard: Chessboard = Chessboard()

    private val computer: Bot = bot
    val playerColor = color
    val toMove = Color.WHITE
    private val moveHistory: Stack<String> = Stack()
    private val futureMoves: Stack<String> = Stack()
    val gameResult = MutableLiveData(GameResult.GAME_IN_PROGRESS)
    var hintsRemaining = 0
    var fiftyMoveCounter = 0
    var moveCounter = 1
    private val positionMap = mutableMapOf<String,Int>()
    var targetSquare: Square? = null



    init {
        chessboard.playerColor = playerColor
    }


    /**
     * starts a new game
     *
     */
    open fun newGame(){
        chessboard.clearBoard()
        moveHistory.clear()
        futureMoves.clear()
        fiftyMoveCounter = 0
        moveCounter = 1
        positionMap.clear()
        gameResult.postValue(GameResult.GAME_IN_PROGRESS)
    }

    /**
     * starts a new game with the same board setup
     * used for puzzles, in base class simply starts a new game
     * @see newGame
     *
     */
    open fun restartGame(){
        newGame()
    }

    /**
     * sets the move timers and sends the fen string to the board to load position
     *
     */
    fun loadPositionFenString(fenString: String){
        val fenList = fenString.split(" ")
        val halfMoveString = fenList[4]
        val fullMoveString = fenList[5]
        chessboard.loadPositionFenString(fenString)
        fiftyMoveCounter = halfMoveString.toInt()
        moveCounter = fullMoveString.toInt()
    }


    /**
     * gets the FEN string positional segment from board and appends move timers
     *
     */
    fun generateFenStringFromPosition(): String {
        var fen = chessboard.getPartialFenStringFromPosition()
        fen += fiftyMoveCounter.toString()
        fen +=" "
        fen += moveCounter.toString()
        return fen
    }

    fun getPositionString(): String {
        return chessboard.getPartialFenStringFromPosition()
        //todo
    }

    /**
     * sends the move to the chessboard and adds move to the move stack to facilitate undo move
     * empties future move history if move is played with moves on the future stack
     * updates game values and performs game result checks
     *
     */
    open fun makeHumanMove(square: Square){
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
                //todo do this for computer
                updateCountersAndCheckForDraw()

                // if move promotes,wait until user chooses promotion piece to call engine
/*                if (!((move.endSquare.row == 0 || move.endSquare.row == 7) && (move.pieceType == PieceType.PAWN))){
                    makeComputerMove()
                }*/
                break
            }
        }
    }



    open fun makeComputerMove(){
        val moveStr = computer.getMove(generateFenStringFromPosition())
        moveHistory.push(generateFenStringFromPosition())
        if (chessboard.turn == Color.BLACK){
            moveCounter ++
        }
        chessboard.makeMove(moveStr)
        updateCountersAndCheckForDraw()

    }

    fun promotePawn(square: Square, pieceType: PieceType, color: Color){
        chessboard.promotePawn(square,pieceType,color)
        makeComputerMove()
    }

    /**
     * removes the move counter data from the string for use in threefold checking
     * @see checkThreefoldRepetition
     */
    private fun stripMoveDataFromFen(fenString: String): String {
        return fenString.split(" ").take(4).joinToString { " " }
    }

    /**
     * adds the given position to threefold map
     * @see checkThreefoldRepetition
     * @see stripMoveDataFromFen
     */
    private fun addPosToThreefoldMap(string: String = getPositionString()){
        positionMap[string] = (positionMap[string] ?:0) +1
    }

    /**
     * removes the given position from threefold map
     * @see checkThreefoldRepetition
     * @see stripMoveDataFromFen
     */
    private fun removePosFromThreefoldMap(string: String = getPositionString()){
        positionMap[string] = (positionMap[string] ?:1) -1
    }

    /**
     * puts current bord position on the future move stack and loads previous position
     * Also removes position from threefold repetition check map
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
            removePosFromThreefoldMap()
            val lastMoveFenString = moveHistory.pop()
            loadPositionFenString(lastMoveFenString)
        }
    }

    /**
     * puts current bord position on the past move stack and loads position from top of future move stack
     * Also re-adds position to threefold repetition check map
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
            addPosToThreefoldMap()
        }
    }

    /**
     * undoes all moves until start position is reached, adding each to the future move stack
     * also adds current position to the stack. Also removes positions from threefold repetition check map
     * @see undoMove
     *
     */
    fun undoAllMoves(){
        if (moveHistory.isEmpty()){
            return
        }
        moveHistory.push(generateFenStringFromPosition())
        while (moveHistory.isNotEmpty()){
            val position = stripMoveDataFromFen(moveHistory.peek())
            removePosFromThreefoldMap(position)
            futureMoves.push(moveHistory.pop())
        }
        loadPositionFenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    }

    /**
     * redoes all moves in the future stack until position after most recent move is reached,
     * adding each to move history stack. Also re-adds positions to threefold repetition check map
     * @see redoMove
     *
     */
    fun redoAllMoves(){
        if (futureMoves.isEmpty()){
            return
        }
        while (futureMoves.isNotEmpty()){
            val position = stripMoveDataFromFen(futureMoves.peek())
            addPosToThreefoldMap(position)
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
            gameResult.postValue(GameResult.WHITE_WIN_RESIGNATION)
        }
        else if (playerColor == Color.WHITE){
            gameResult.postValue(GameResult.BLACK_WIN_RESIGNATION)
        }
    }

    /**
     * Increments or resets the fifty move counter and sets a draw if 50 moves ( 100 half moves) is reached
     * @see fiftyMoveCounter
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

    /**
     * sets draw if three move repetition has happened
     * @see positionMap
     */
    private fun checkThreefoldRepetition(){
        if (positionMap.containsValue(3)){
            gameResult.postValue(GameResult.DRAW_BY_REPETITION)
        }
    }

    /**
     * sets draw if there is insufficient material.
     * Returns immediately when sufficient material is found
     * @param fenString current game state in FEN string
     * @see generateFenStringFromPosition
     */
    private fun checkForInsufficientMaterial(fenString: String) {
        //get the first segment of FEN only
        val boardString = fenString.split(' ').first()
        var blackMaterial = 0
        var whiteMaterial = 0
        for (char in boardString) {
            if (char.isUpperCase()){
                // white
                when (char) {
                    'P' -> return
                    'N' -> whiteMaterial += 3
                    'B' -> whiteMaterial += 3
                    'R' -> return
                    'Q' -> return
                }
            }
            else if (char.isLowerCase()){
                //black
                when (char) {
                    'p' -> return
                    'n' -> blackMaterial += 3
                    'b' -> blackMaterial += 3
                    'r' -> return
                    'q' -> return
                }
            }
        }
        if (whiteMaterial < 4 && blackMaterial < 4 ){
            gameResult.postValue(GameResult.DRAW_BY_INSUFFICIENT)
        }
    }

    /**
     * calls  draw check functions
     *
     * @see checkForInsufficientMaterial
     * @see checkThreefoldRepetition
     */
    private fun updateCountersAndCheckForDraw(){
        val fenString = generateFenStringFromPosition()
        addPosToThreefoldMap(fenString)
        checkThreefoldRepetition()
        checkForInsufficientMaterial(fenString)
    }

    fun  searchComputerMoves(){

        val pos = fenToEnginePos(generateFenStringFromPosition())
        //val search = Search(pos,longList,1, TranspositionTable(1), History())

        val comp = ComputerPlayer()
        val movesList = comp.returnTopMoves(pos,250)

        for (i in movesList){
            Log.d("test",i.first.toString())
            Log.d("test",i.second.toString())
        }
    }



}