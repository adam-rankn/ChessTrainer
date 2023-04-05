package com.pinguapps.chesstrainer.logic

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pinguapps.chesstrainer.data.*
import com.pinguapps.chesstrainer.logic.bots.BasicBot
import com.pinguapps.chesstrainer.logic.bots.Bot
import java.util.*


open class Chessgame(color: Color = Color.WHITE, bot: Bot = BasicBot()) {

    val chessboard: Chessboard = Chessboard()


    private val computer: Bot = bot
    var playerColor = color
        set(value) {
            when (value) {
                Color.WHITE -> {
                    this.cpuColor = Color.BLACK
                    this.playerIsBlack.value = false
                }
                Color.BLACK -> {
                    this.cpuColor = Color.WHITE
                    this.playerIsBlack.value = true
                }
                Color.NONE -> {
                    this.cpuColor = Color.NONE
                    this.playerIsBlack.value = false
                }
            }
            field = value
        }

    val playerIsBlack: MutableLiveData<Boolean> = MutableLiveData(false)
    var cpuColor = Color.BLACK
    val toMove: Color get() = chessboard.turn
    private val moveHistory: Stack<String> = Stack()
    private val futureMoves: Stack<String> = Stack()
    val gameResult = MutableLiveData(GameResult.GAME_IN_PROGRESS)
    var hintsRemaining = 0
    var fiftyMoveCounter = 0
    var moveCounter = 1
    private val positionMap = mutableMapOf<String,Int>()
    var targetSquare: Square? = null
    val lastPosition: MutableLiveData<String> = MutableLiveData("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")



    init {
        chessboard.playerColor = playerColor
    }


    /**
     * starts a new game
     *
     */
    open fun newGame(){
        chessboard.clearBoard()
        chessboard.resetBoard()
        moveHistory.clear()
        futureMoves.clear()
        fiftyMoveCounter = 0
        moveCounter = 1
        positionMap.clear()
        gameResult.postValue(GameResult.GAME_IN_PROGRESS)
        lastPosition.value = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
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

    private fun getPositionString(): String {
        return chessboard.getPartialFenStringFromPosition()
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
                doMove(move)
                break
            }
        }
    }

    fun makeMove(uci: String){
        //todo test
        val start = uci.slice(IntRange(0,1))
        val end = uci.slice(IntRange(2,3))


        val promotion: PieceType = if (uci.length == 5){
            when (uci[4]) {
                'q' -> PieceType.QUEEN
                'r' -> PieceType.ROOK
                'n' -> PieceType.KNIGHT
                'b' -> PieceType.BISHOP
                else -> PieceType.NONE
            }
        } else {
            PieceType.NONE
        }
        val startSquare = chessboard.getSquare(start)
        val endSquare = chessboard.getSquare(end)
        val color = startSquare.pieceColor

        val castling = when (uci) {
            "e1h1" -> Castleing.WHITE_KING
            "e1a1" -> Castleing.WHITE_QUEEN
            "e8h8" -> Castleing.BLACK_KING
            "e8a8" -> Castleing.BLACK_QUEEN
            else -> Castleing.NONE
        }

        val move = Move(
            endSquare = endSquare,
            startSquare = startSquare,
            pieceType = startSquare.pieceType,
            capturedPiece = endSquare.pieceType,
            castling = castling
            )
        doMove(move)
        if (promotion != PieceType.NONE){
            chessboard.promotePawn(
                color = color,
                pieceType = promotion,
                square = endSquare
            )
        }
    }

    private fun doMove(move: Move){
        moveHistory.push(generateFenStringFromPosition())
        if (chessboard.turn == Color.BLACK){
            moveCounter ++
        }
        chessboard.selectedSquare = move.startSquare // needed in case of speech to text
        chessboard.makeMove(move)
        futureMoves.clear()
        updateFiftyMoveCounter(move)
        updateCountersAndCheckForDraw()

        //pawn promotion
        if (move.pieceType == PieceType.PAWN) {
            if (move.endSquare.row == 0) {
                setPawnPromoted(move.endSquare)
            }
            else if (move.endSquare.row == 7){
                setPawnPromoted(move.endSquare)
            }
        }
        lastPosition.postValue(generateFenStringFromPosition())
    }

    open fun setPawnPromoted(square: Square){
        chessboard.promotionSquare.postValue(square)
    }

    open fun makeComputerMove(){
        val moveStr = computer.getMove(generateFenStringFromPosition())
        moveHistory.push(generateFenStringFromPosition())
        if (chessboard.turn == Color.BLACK){
            moveCounter ++
        }
        makeMove(moveStr)
        updateCountersAndCheckForDraw()

        //pawn promotion
        if ('q' in moveStr.lowercase()) {
            setPawnPromoted(chessboard.getSquare(moveStr.slice(IntRange(2,3))))
        }
    }

    open fun promotePawn(square: Square, pieceType: PieceType, color: Color){
        chessboard.promotePawn(square,pieceType,color)
        //makeComputerMove()
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
     * puts current board position on the future move stack and loads previous position
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
            lastPosition.value = lastMoveFenString
        }
    }

    /**
     * puts current board position on the past move stack and loads position from top of future move stack
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
            lastPosition.value = futureMoves.peek()
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
        Log.d("undoall","pushing current position")
        moveHistory.push(generateFenStringFromPosition())
        while (moveHistory.size > 1){
            val position = stripMoveDataFromFen(moveHistory.peek())
            removePosFromThreefoldMap(position)
            futureMoves.push(moveHistory.pop())
        }
        loadPositionFenString(moveHistory.peek())
        lastPosition.value = moveHistory.peek()
        removePosFromThreefoldMap()
        moveHistory.pop()



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
        moveHistory.push(generateFenStringFromPosition())
        addPosToThreefoldMap()
        while (futureMoves.size > 1){
            val position = stripMoveDataFromFen(futureMoves.peek())
            addPosToThreefoldMap(position)
            moveHistory.push(futureMoves.pop())
        }
        lastPosition.value = futureMoves.peek()
        loadPositionFenString(futureMoves.pop())
        addPosToThreefoldMap()

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


}