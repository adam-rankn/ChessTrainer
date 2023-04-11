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
    private val positionHistory: Stack<String> = Stack()
    private val futurePositions: Stack<String> = Stack()
    val gameResult = MutableLiveData(GameResult.GAME_IN_PROGRESS)
    var hintsRemaining = 0
    var fiftyMoveCounter = 0
    var moveCounter = 1
    private val positionMap = mutableMapOf<String, Int>()
    var targetSquare: Square? = null
    val lastPosition: MutableLiveData<String> =
        MutableLiveData("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    val lastMoves: Stack<Move> = Stack()
    private val futureMoves: Stack<Move> = Stack()


    init {
        chessboard.playerColor = playerColor
    }


    /**
     * starts a new game
     *
     */
    open fun newGame() {
        chessboard.clearBoard()
        chessboard.resetBoard()
        positionHistory.clear()
        futurePositions.clear()
        fiftyMoveCounter = 0
        moveCounter = 1
        positionMap.clear()
        gameResult.postValue(GameResult.GAME_IN_PROGRESS)
        lastPosition.value = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        lastMoves.clear()
    }

    /**
     * starts a new game with the same board setup
     * used for puzzles, in base class simply starts a new game
     * @see newGame
     *
     */
    open fun restartGame() {
        newGame()
    }

    /**
     * sets the move timers and sends the fen string to the board to load position
     *
     */
    fun loadPositionFenString(fenString: String) {
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
        fen += " "
        fen += moveCounter.toString()
        return fen
    }


    /**
     * makes a move from user input
     *
     */
    open fun makeHumanMove(square: Square) {
        for (move in chessboard.validMoves) {
            if (move.endSquare == square) {
                doMove(move)
                break
            }
        }
    }

    /**
     * takes a UCI string and generates a move object, then makes the move
     * @param uci the move in UCI format
     */
    fun makeMove(uci: String) {
        //todo test
        //todo en peasant
        val start = uci.slice(IntRange(0, 1))
        val end = uci.slice(IntRange(2, 3))

        val promotion: PieceType = if (uci.length == 5) {
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
            "e1g1c" -> Castleing.WHITE_KING
            "e1c1c" -> Castleing.WHITE_QUEEN
            "e8g8c" -> Castleing.BLACK_KING
            "e8c8c" -> Castleing.BLACK_QUEEN
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
        if (promotion != PieceType.NONE) {
            chessboard.promotePawn(
                color = color,
                pieceType = promotion,
                square = endSquare
            )
        }
    }

    /**
     * moves a piece
     */
    private fun doMove(move: Move) {
        positionHistory.push(generateFenStringFromPosition())
        if (chessboard.turn == Color.BLACK) {
            moveCounter++
        }
        chessboard.selectedSquare = move.startSquare // needed in case of speech to text
        chessboard.makeMove(move)
        futurePositions.clear()
        updateFiftyMoveCounter(move)
        updateCountersAndCheckForDraw()

        //pawn promotion
        if (move.pieceType == PieceType.PAWN) {
            if (move.endSquare.row == 0) {
                setPawnPromoted(move.endSquare)
            } else if (move.endSquare.row == 7) {
                setPawnPromoted(move.endSquare)
            }
        }
        lastPosition.postValue(generateFenStringFromPosition())
        lastMoves.add(move)
    }

    /**
     *  sets the promotion square so that user can choose piece to promote to
     */
    open fun setPawnPromoted(square: Square) {
        chessboard.promotionSquare.postValue(square)
    }

    open fun makeComputerMove() {
        val moveStr = computer.getMove(generateFenStringFromPosition())
        positionHistory.push(generateFenStringFromPosition())
        if (chessboard.turn == Color.BLACK) {
            moveCounter++
        }
        makeMove(moveStr)
        updateCountersAndCheckForDraw()

        //pawn promotion
        if ('q' in moveStr.lowercase()) {
            setPawnPromoted(chessboard.getSquare(moveStr.slice(IntRange(2, 3))))
        }
    }

    /**
     * immediately promotes a pawn on square to specified piece
     */
    open fun promotePawn(square: Square, pieceType: PieceType, color: Color) {
        chessboard.promotePawn(square, pieceType, color)
        //makeComputerMove()
    }

    /**
     * removes the move counter data from the string for use in threefold checking
     * @see checkThreefoldRepetition
     */
    private fun stripMoveDataFromFen(fenString: String): String {
        return fenString.split(" ").take(4).toString()
    }

    /**
     * adds the given position to threefold map
     * @see checkThreefoldRepetition
     * @see stripMoveDataFromFen
     */
    private fun addPosToThreefoldMap(string: String = generateFenStringFromPosition()) {
        val pos = stripMoveDataFromFen(string)
        positionMap[pos] = (positionMap[pos] ?: 0) + 1
    }

    /**
     * removes the given position from threefold map
     * @see checkThreefoldRepetition
     * @see stripMoveDataFromFen
     * @see undoMove
     */
    private fun removePosFromThreefoldMap(string: String = generateFenStringFromPosition()) {
        val pos = stripMoveDataFromFen(string)
        positionMap[pos] = (positionMap[pos] ?: 1) - 1
    }

    /**
     * puts current board position on the future move stack and loads previous position
     * Also removes position from threefold repetition check map
     * @see redoMove
     *
     */
    fun undoMove() {
        if (positionHistory.isEmpty()) {
            return
        }
        if (positionHistory.isNotEmpty()) {
            val currentBoardPosition = generateFenStringFromPosition()
            futurePositions.push(currentBoardPosition)
            removePosFromThreefoldMap()
            val lastMoveFenString = positionHistory.pop()
            loadPositionFenString(lastMoveFenString)
            lastPosition.value = lastMoveFenString
            futureMoves.push(lastMoves.pop())
        }
    }

    /**
     * puts current board position on the past move stack and loads position from top of future move stack
     * Also re-adds position to threefold repetition check map
     * @see undoMove
     *
     */
    fun redoMove() {
        if (futurePositions.isEmpty()) {
            return
        }
        if (futurePositions.isNotEmpty()) {
            val currentBoardPosition = generateFenStringFromPosition()
            positionHistory.push(currentBoardPosition)
            lastPosition.value = futurePositions.peek()
            loadPositionFenString(futurePositions.pop())
            addPosToThreefoldMap()
            lastMoves.push(futureMoves.pop())

        }
    }

    /**
     * undoes all moves until start position is reached, adding each to the future move stack
     * also adds current position to the stack. Also removes positions from threefold repetition check map
     * @see undoMove
     *
     */
    fun undoAllMoves() {
        if (positionHistory.isEmpty()) {
            return
        }
        positionHistory.push(generateFenStringFromPosition())
        while (positionHistory.size > 1) {
            val position = stripMoveDataFromFen(positionHistory.peek())
            removePosFromThreefoldMap(position)
            futurePositions.push(positionHistory.pop())
            futureMoves.push(lastMoves.pop())
        }
        loadPositionFenString(positionHistory.peek())
        lastPosition.value = positionHistory.peek()
        removePosFromThreefoldMap()
        positionHistory.pop()
    }

    /**
     * redoes all moves in the future stack until position after most recent move is reached,
     * adding each to move history stack. Also re-adds positions to threefold repetition check map
     * @see redoMove
     *
     */
    fun redoAllMoves() {
        if (futurePositions.isEmpty()) {
            return
        }
        positionHistory.push(generateFenStringFromPosition())
        addPosToThreefoldMap()
        while (futurePositions.size > 1) {
            val position = stripMoveDataFromFen(futurePositions.peek())
            addPosToThreefoldMap(position)
            positionHistory.push(futurePositions.pop())
            lastMoves.push(futureMoves.pop())
        }
        lastMoves.push(futureMoves.pop())
        lastPosition.value = futurePositions.peek()
        loadPositionFenString(futurePositions.pop())
        addPosToThreefoldMap()
        //TODO test last moves and other functionality

    }

    /**
     * resigns the current game
     *
     */
    fun resign() {
        if (playerColor == Color.BLACK) {
            gameResult.postValue(GameResult.WHITE_WIN_RESIGNATION)
        } else if (playerColor == Color.WHITE) {
            gameResult.postValue(GameResult.BLACK_WIN_RESIGNATION)
        }
    }

    fun isPlayerTurn(): Boolean {
        return toMove == playerColor
    }

    fun isCpuTurn(): Boolean {
        return toMove == cpuColor
    }

    /**
     * Increments or resets the fifty move counter and sets a draw if 50 moves ( 100 half moves) is reached
     * @see fiftyMoveCounter
     */
    private fun updateFiftyMoveCounter(move: Move) {
        if (move.capturedPiece != PieceType.NONE || move.pieceType == PieceType.PAWN) {
            fiftyMoveCounter = 0
        } else {
            fiftyMoveCounter++
        }
        if (fiftyMoveCounter == 100) {
            gameResult.postValue(GameResult.DRAW_BY_FIFTY)
        }
    }

    /**
     * sets draw if three move repetition has happened
     * @see positionMap
     */
    private fun checkThreefoldRepetition() {
        if (positionMap.containsValue(3)) {
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
            if (char.isUpperCase()) {
                // white
                when (char) {
                    'P' -> return
                    'N' -> whiteMaterial += 3
                    'B' -> whiteMaterial += 3
                    'R' -> return
                    'Q' -> return
                }
            } else if (char.isLowerCase()) {
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
        if (whiteMaterial < 4 && blackMaterial < 4) {
            gameResult.postValue(GameResult.DRAW_BY_INSUFFICIENT)
        }
    }

    /**
     * calls  draw check functions
     *
     * @see checkForInsufficientMaterial
     * @see checkThreefoldRepetition
     */
    private fun updateCountersAndCheckForDraw() {
        val fenString = generateFenStringFromPosition()
        addPosToThreefoldMap(fenString)
        checkThreefoldRepetition()
        checkForInsufficientMaterial(fenString)
    }


    fun importGameFromPgn(string: String): MutableList<String> {

        //remove comments
        val string2 = string as CharSequence
        val pgnString = string2.replace(Regex("\\{[^>]*}"), "")
        val pgn = (pgnString.split(" ") as MutableList<String>)
        while (pgn.contains("")) {
            pgn.remove("")
        }
        val parsedPgnList: MutableList<String> = mutableListOf()
        println(pgn.toString())
        while (pgn.isNotEmpty()) {
            val nextThree = pgn.take(3)
            println(nextThree.toString())
            parsedPgnList.add(nextThree.joinToString(" ").trim())
            pgn.removeAll(nextThree)
        }
        return parsedPgnList
    }

}