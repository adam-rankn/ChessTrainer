package com.pinguapps.chesstrainer.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlin.math.abs
import kotlin.math.min


class Chessboard {

    //todo move some stuff to chessgame
    val board = Array(8) { row ->
        Array(8) { col ->
            Square(col, row)
        }
    }

    var playerColor = Color.WHITE

    var selectedSquare : Square? = null
    var validMoves = mutableListOf<Move>()
    var turn = Color.WHITE

    var whiteCastleQueenRights = true
    var whiteCastleKingRights  = true
    var blackCastleQueenRights = true
    var blackCastleKingRights  = true
    var enPassantSquare: Square? = null
    val result = MutableLiveData(GameResult.GAME_IN_PROGRESS)
    var whiteKingSquare = getSquare("e1")
    var blackKingSquare = getSquare("e8")
    var whiteInCheck = false
    var blackInCheck = false
    private val positionMap = mutableMapOf<String,Int>()
    var promotionSquare = MutableLiveData<Square>()

    init {
        loadPositionFenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
        whiteCastleQueenRights = true
        whiteCastleKingRights  = true
        blackCastleQueenRights = true
        blackCastleKingRights  = true

    }

    fun clearBoard() {
        for (col in board){
            for (square in col){
                square.piece = Piece(Color.NONE,PieceType.NONE)
            }
        }



        whiteCastleQueenRights = false
        whiteCastleKingRights  = false
        blackCastleQueenRights = false
        blackCastleKingRights  = false
    }

    fun getSquare(notation: String): Square {
        val colStr: String = notation.substring(0,1)
        val rowStr: String = notation.substring(1,2)

        val row = abs(rowStr.toInt() - 8)
        val col = when (colStr){
            "a" -> 0
            "b" -> 1
            "c" -> 2
            "d" -> 3
            "e" -> 4
            "f" -> 5
            "g" -> 6
            "h" -> 7

            else -> 0
        }

        return (board[col][row])
    }

    fun getSquare(col: Int, row: Int): Square {
        return board[col][row]
    }

    fun getPieceOnSquare(notation: String): Piece {
        return getSquare(notation).piece
    }

   private fun testMakeMove(move: Move){

        val startSquare = move.startSquare
        val endSquare = move.endSquare
        val startCol = startSquare.col
        val startRow = startSquare.row
        val piece = board[startCol][startRow].piece
        val endCol = endSquare.col
        val endRow = endSquare.row

        // remove en passanted pawn
        if (move.endSquare.pieceType == PieceType.NONE && move.capturedPiece != PieceType.NONE){
            if (turn == Color.WHITE){
                board[move.endSquare.col][move.endSquare.row +1].piece = Piece(Color.NONE,PieceType.NONE)
            }
            else {
                board[move.endSquare.col][move.endSquare.row -1].piece = Piece(Color.NONE,PieceType.NONE)
            }
        }
        board[startCol][startRow].piece = Piece(Color.NONE, PieceType.NONE)
        placePiece(board[endCol][endRow], piece.color, piece.type)
        if (piece.type == PieceType.KING){
            if (piece.color == Color.WHITE){
                whiteKingSquare = board[endCol][endRow]
            }
            else if (piece.color == Color.BLACK) {
                blackKingSquare = board[endCol][endRow]
            }
        }

    }

    fun isMoveValid(square: Square): Boolean {
        for (move in validMoves){
            if (move.endSquare == square){
                return true
            }
        }
        return false
    }

    fun makeMove(move: Move){
                val endSquare = move.endSquare
                // remove en passanted pawn
                if (move.endSquare.pieceType == PieceType.NONE && move.capturedPiece != PieceType.NONE){
                    doEnPassantMove(move)
                }
                //make move
                if (selectedSquare != null){
                    endSquare.piece = selectedSquare!!.piece
                    selectedSquare!!.piece = Piece(Color.NONE, PieceType.NONE)
                }

                //pawn promotion
                if (move.pieceType == PieceType.PAWN) {
                    if (move.endSquare.row == 0) {
                        promotionSquare.postValue(move.endSquare)
                    }
                    else if (move.endSquare.row == 7){
                        promotionSquare.postValue(move.endSquare)
                    }
                }

                if (move.castling != Castleing.NONE){
                    doCastleMove(move)
                }// castleing

                selectedSquare = null
                validMoves = mutableListOf()
                enPassantSquare = null
                if (move.enPassantSquare != null) {
                    enPassantSquare = move.enPassantSquare
                }
                if (move.pieceType == PieceType.KING){
                    updateKingPosition(move)
                }

        if (turn == Color.WHITE){
            checkForPins(blackKingSquare)
            blackInCheck = isKingInCheck(blackKingSquare)
            whiteInCheck = false
            turn = Color.BLACK
        }
        else {
            checkForPins(whiteKingSquare)
            whiteInCheck = isKingInCheck(whiteKingSquare)
            blackInCheck = false
            turn = Color.WHITE
        }
        checkThreefoldRepetition()
        //todo update checkmate state
    }

    fun makeMove(square: Square){
        for (move in validMoves){
            if (move.endSquare == square){
                makeMove(move)
            }
        }
    }

    /**
     * checks piece type and calls appropriate move gen function
     */
    fun generatePieceMoves(square: Square): MutableList<Move> {
        val potentialMoves = mutableListOf<Move>()
         when (square.pieceType) {
            PieceType.PAWN -> {
                potentialMoves.addAll(generateValidPawnMoves(square))
            }
            PieceType.BISHOP -> {
                potentialMoves.addAll(generateBishopMoves(square))
            }
            PieceType.KNIGHT -> {
                potentialMoves.addAll(generateValidKnightMoves(square))
            }
            PieceType.ROOK -> {
                potentialMoves.addAll(generateRookMoves(square))
            }
            PieceType.QUEEN -> {
                potentialMoves.addAll(generateQueenMoves(square))
            }
            PieceType.KING -> {
                potentialMoves.addAll(generateKingMoves(square))
            }
            else -> {}
        }

        val nonCheckMoves = mutableListOf<Move>()
        for (move in potentialMoves){
            val testBoard = Chessboard()
            val fen = getPartialFenStringFromPosition()
            testBoard.loadPositionFenString(fen)
            testBoard.testMakeMove(move)
            if ((turn == Color.WHITE) && !(testBoard.isKingInCheck(testBoard.whiteKingSquare))){
                nonCheckMoves.add(move)
            }
            else if ((turn == Color.BLACK) && !(testBoard.isKingInCheck(testBoard.blackKingSquare))){
                nonCheckMoves.add(move)
            }

        }

        return nonCheckMoves
    }

    private fun placePiece(square: Square, color: Color, type: PieceType) {
        square.piece = Piece(color,type)
    }

    /**
     * Generates valid horizontal or vertical sliding moves moves for rook or queen on square
     */
    fun generateRookMoves(rookSquare: Square): MutableList<Move> {
        val validMoves = mutableListOf<Move>()
        val row = rookSquare.row
        val col = rookSquare.col
        val pinned = rookSquare.piece.pinned
        val ownColor = rookSquare.pieceColor
        var squaresMoved = 1
        val opponentColor = if (ownColor == Color.WHITE){
            Color.BLACK
        } else {
            Color.WHITE
        }

        if (pinned == PinnedState.NONE || pinned == PinnedState.HORIZONTAL) {
            //up moves
            while (col + squaresMoved <= 7) {
                val targetSquare = board[col + squaresMoved][row]
                if (targetSquare.pieceColor== opponentColor){
                    val move = Move(startSquare = rookSquare, endSquare = targetSquare,
                        pieceType = PieceType.ROOK, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.pieceColor == ownColor){
                    break
                }
                else if (targetSquare.pieceType == PieceType.NONE){
                    val move = Move(startSquare = rookSquare, endSquare = targetSquare,
                        pieceType = PieceType.ROOK, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }

            squaresMoved = 1
            //down moves
            while (col - squaresMoved >= 0) {
                val targetSquare = board[col - squaresMoved][row]
                if (targetSquare.pieceColor == opponentColor){
                    val move = Move(startSquare = rookSquare, endSquare = targetSquare,
                        pieceType = PieceType.ROOK, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.pieceColor == ownColor){
                    break
                }
                else if (targetSquare.pieceType == PieceType.NONE){
                    val move = Move(startSquare = rookSquare, endSquare = targetSquare,
                        pieceType = PieceType.ROOK, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }
        }


        if (pinned == PinnedState.NONE || pinned == PinnedState.VERTICAL) {
            //right moves
            squaresMoved = 1
            while (row + squaresMoved <= 7) {
                val targetSquare = board[col][row + squaresMoved]
                if (targetSquare.pieceColor == opponentColor){
                    val move = Move(startSquare = rookSquare, endSquare = targetSquare,
                        pieceType = PieceType.ROOK, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.pieceColor == ownColor){
                    break
                }
                else if (targetSquare.pieceType == PieceType.NONE){
                    val move = Move(startSquare = rookSquare, endSquare = targetSquare,
                        pieceType = PieceType.ROOK, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }
            //left moves
            squaresMoved = 1
            while (row - squaresMoved >= 0) {
                val targetSquare = board[col][row - squaresMoved]
                if (targetSquare.pieceColor== opponentColor){
                    val move = Move(startSquare = rookSquare, endSquare = targetSquare,
                        pieceType = PieceType.ROOK, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.pieceColor == ownColor){
                    break
                }
                else if (targetSquare.pieceType == PieceType.NONE){
                    val move = Move(startSquare = rookSquare, endSquare = targetSquare,
                        pieceType = PieceType.ROOK, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }
        }

        return validMoves
    }

    /**
     * Checks for pins by searching in each direction from king, and updates pinned flag on each piece
     */
    fun checkForPins(kingSquare: Square) {
        //todo use actual king piece, track king locations on board
        for (col in board){
            for (square in col){
                square.piece.pinned = PinnedState.NONE
            }
        }
        val ownColor = kingSquare.pieceColor
        val opponentColor = if (ownColor == Color.WHITE){
            Color.BLACK
        } else {
            Color.WHITE
        }
        val row = kingSquare.row
        val col = kingSquare.col
        val movesUp = 7-row
        var ownPieces = 0
        var potentialPinnedPiece = Piece(Color.NONE,PieceType.NONE)

        //up from king
        for (i in 1..movesUp) {
            val piece = board[col][row+i].piece
            if (piece.color == ownColor && ownPieces == 0){
                ownPieces = 1
                potentialPinnedPiece = piece
            }
            else if (piece.color == ownColor){
                break
            }
            else if (piece.type == PieceType.ROOK
                    || piece.type == PieceType.QUEEN
                    && piece.color == opponentColor) {
                potentialPinnedPiece.pinned = PinnedState.VERTICAL
            }
        }

        //down from king
        ownPieces = 0
        potentialPinnedPiece = Piece(Color.NONE,PieceType.NONE)
        for (i in 1..row){
            val piece = board[col][row-i].piece
            if (piece.color == ownColor && ownPieces == 0){
                ownPieces = 1
                potentialPinnedPiece = piece
            }
            else if (piece.color == ownColor){
                break
            }
            else if (piece.type == PieceType.ROOK
                || piece.type == PieceType.QUEEN
                && piece.color == opponentColor){
                potentialPinnedPiece.pinned = PinnedState.VERTICAL

            }
        }

        //right from king
        ownPieces = 0
        potentialPinnedPiece = Piece(Color.NONE,PieceType.NONE)
        val movesRight = 7-col
        for (i in 1..movesRight){
            val piece = board[col+i][row].piece

            if (piece.color == ownColor && ownPieces == 0){
                ownPieces = 1
                potentialPinnedPiece = piece
            }
            else if (piece.color == ownColor){
                break
            }
            else if (piece.type == PieceType.ROOK
                || piece.type == PieceType.QUEEN
                && piece.color == opponentColor){
                potentialPinnedPiece.pinned = PinnedState.HORIZONTAL
            }
        }

        //left from king
        ownPieces = 0
        potentialPinnedPiece = Piece(Color.NONE,PieceType.NONE)
        for (i in 1..col){
            val piece = board[col-i][row].piece
            if (piece.color == ownColor && ownPieces == 0){
                ownPieces = 1
                potentialPinnedPiece = piece
            }
            else if (piece.color == ownColor){
                break
            }
            else if (piece.type == PieceType.ROOK
                || piece.type == PieceType.QUEEN
                && piece.color == opponentColor) {
                potentialPinnedPiece.pinned = PinnedState.HORIZONTAL

            }
        }

        // up right from king
        ownPieces = 0
        potentialPinnedPiece = Piece(Color.NONE,PieceType.NONE)
        val movesUpRight = min(7-row, 7-col)
        for (i in 1..movesUpRight){
            val piece = board[col+i][row+i].piece
            if (piece.color == ownColor && ownPieces == 0){
                ownPieces = 1
                potentialPinnedPiece = piece
            }
            else if (piece.color == ownColor){
                break
            }
            else if (piece.type == PieceType.BISHOP||
                piece.type == PieceType.QUEEN
                && piece.color == opponentColor) {
                potentialPinnedPiece.pinned = PinnedState.DIAGONALA8H1

            }
        }

        //up left from king
        ownPieces = 0
        potentialPinnedPiece = Piece(Color.NONE,PieceType.NONE)
        val movesUpLeft = min(7-col,row)
        for (i in 1..movesUpLeft){
            val piece = board[col+i][row-i].piece
            if (piece.color == ownColor && ownPieces == 0){
                ownPieces = 1
                potentialPinnedPiece = piece
            }
            else if (piece.color == ownColor){
                break
            }
            else if (piece.type == PieceType.BISHOP||
                piece.type == PieceType.QUEEN
                && piece.color == opponentColor) {
                potentialPinnedPiece.pinned = PinnedState.DIAGONALA1H8
            }
        }
        // down left from king
        ownPieces = 0
        potentialPinnedPiece = Piece(Color.NONE,PieceType.NONE)
        val movesDownLeft = min(col,row)
        for (i in 1..movesDownLeft) {
            val piece = board[col - i][row - i].piece
            if (piece.color == ownColor && ownPieces == 0){
                ownPieces = 1
                potentialPinnedPiece = piece
            }
            else if (piece.color == ownColor){
                break
            }
            else if (piece.type == PieceType.BISHOP ||
                piece.type == PieceType.QUEEN
                && piece.color == opponentColor) {
                potentialPinnedPiece.pinned = PinnedState.DIAGONALA8H1
            }
        }
        //down right from king
        ownPieces = 0
        potentialPinnedPiece = Piece(Color.NONE,PieceType.NONE)
        val movesDownRight = min(col,7-row)
        for (i in 1..movesDownRight){
            val piece = board[col-i][row+i].piece
            if (piece.color == ownColor && ownPieces == 0){
                ownPieces = 1
                potentialPinnedPiece = piece
            }
            else if (piece.color == ownColor){
                break
            }
            else if (piece.type == PieceType.BISHOP||
                piece.type == PieceType.QUEEN
                && piece.color == opponentColor) {
                potentialPinnedPiece.pinned = PinnedState.DIAGONALA1H8
            }
        }
    }

    /**
     * Generates valid moves for knight on square
     */
    fun generateValidKnightMoves(knightSquare: Square): MutableList<Move> {
        val col = knightSquare.col
        val row = knightSquare.row
        val ownColor = knightSquare.pieceColor
        val opponentColor = if (ownColor == Color.WHITE){
            Color.BLACK
        } else {
            Color.WHITE
        }
        val validMoves = mutableListOf<Move>()
        if (knightSquare.piece.pinned == PinnedState.NONE) {
            val knightMoves = listOf(
                Pair(-2,-1), Pair(-2, +1),
                Pair(+2, -1), Pair(+2, +1),
                Pair(-1, +2), Pair(-1, -2),
                Pair(+1, -2), Pair(+1, +2)
            )
            for (move in knightMoves) {
                if (col + move.first in 0..7 && row + move.second in 0..7) {
                    val targetSquare = board[col + move.first][row + move.second]
                    if (targetSquare.pieceColor != ownColor) {
                        val newMove = Move(startSquare = knightSquare, endSquare = targetSquare,
                            pieceType = PieceType.KNIGHT, capturedPiece = targetSquare.pieceType)
                        validMoves.add(newMove)
                    }
                }
            }
        }
        return validMoves
    }

    /**
     * checks the pinned state of pawn and generates valid moves using helper functions
     * @see generatePawnPushMoves
     * @see generatePawnCaptures
     */
    fun generateValidPawnMoves(pawnSquare: Square): MutableList<Move> {
        val validMoves = mutableListOf<Move>()
        when (pawnSquare.piece.pinned) {
            PinnedState.NONE -> {
                //push pawns or cap
                validMoves.addAll(generatePawnPushMoves(pawnSquare))
                validMoves.addAll(generatePawnCaptures(pawnSquare))
            }
            PinnedState.VERTICAL -> {
                validMoves.addAll(generatePawnPushMoves(pawnSquare))
                //push only
            }
            PinnedState.DIAGONALA1H8 -> {
                // capture only
                validMoves.addAll(generatePawnCaptures(pawnSquare))
            }
            PinnedState.DIAGONALA8H1 -> {
                // capture only
                validMoves.addAll(generatePawnCaptures(pawnSquare))
            }
            PinnedState.HORIZONTAL -> {}
        }
        return validMoves
    }

    /**
     * Generates push moves for the pawn on square, including en passant moves
     */
    private fun generatePawnPushMoves(pawnSquare: Square): MutableList<Move> {
        val col = pawnSquare.col
        val row = pawnSquare.row
        val ownColor = pawnSquare.pieceColor
        val validMoves = mutableListOf<Move>()
        if (pawnSquare.row == 6 && ownColor == Color.WHITE
            && board[col][row-1].pieceType == PieceType.NONE
            && board[col][row-2].pieceType == PieceType.NONE){
            val move = Move(startSquare = pawnSquare, endSquare = board[col][row-2],
                pieceType = PieceType.PAWN, enPassantSquare = board[col][row-1],
                capturedPiece = PieceType.NONE)
            validMoves.add(move)
        }
        else if (pawnSquare.row == 1 && ownColor == Color.BLACK
            && board[col][row+1].pieceType == PieceType.NONE
            && board[col][row+2].pieceType == PieceType.NONE) {
            val move = Move(startSquare = pawnSquare, endSquare = board[col][row+2],
                pieceType = PieceType.PAWN, enPassantSquare = board[col][row+1],
                capturedPiece = PieceType.NONE)
            validMoves.add(move)
        }
        if (ownColor == Color.WHITE && board[col][row-1].pieceType == PieceType.NONE){
            val move = Move(startSquare = pawnSquare, endSquare = board[col][row-1],
                pieceType = PieceType.PAWN, capturedPiece = PieceType.NONE)
            validMoves.add(move)
        }
        else if (ownColor == Color.BLACK && board[col][row+1].pieceType == PieceType.NONE) {
            val move = Move(startSquare = pawnSquare, endSquare = board[col][row+1],
                pieceType = PieceType.PAWN, capturedPiece = PieceType.NONE)
            validMoves.add(move)
        }
        return  validMoves
    }

    /**
     * Generates capture moves for the pawn on square, including en passant moves
     */
    private fun generatePawnCaptures(pawnSquare: Square): MutableList<Move> {
        val col = pawnSquare.col
        val row = pawnSquare.row
        val ownColor = pawnSquare.pieceColor
        val validMoves = mutableListOf<Move>()

        if (ownColor == Color.WHITE) {
            if ((pawnSquare.piece.pinned == PinnedState.NONE ||
                pawnSquare.piece.pinned == PinnedState.DIAGONALA1H8) && col != 7
            ) {
                val attackRightSquare = board[col+1][row-1]
                if (attackRightSquare.pieceColor == Color.BLACK){
                    val move = Move(startSquare = pawnSquare, endSquare = attackRightSquare,
                        pieceType = PieceType.PAWN, capturedPiece = attackRightSquare.pieceType)
                    validMoves.add(move)
                }
                if (attackRightSquare == enPassantSquare) {
                    val move = Move(startSquare = pawnSquare, endSquare = attackRightSquare,
                        pieceType = PieceType.PAWN, enPassantSquare = attackRightSquare,
                        capturedPiece = PieceType.PAWN)
                    validMoves.add(move)
                    //todo fold in
                }
            }
            if ((pawnSquare.piece.pinned == PinnedState.NONE ||
                pawnSquare.piece.pinned == PinnedState.DIAGONALA8H1) && col != 0
            ) {
                val attackLeftSquare = board[col-1][row-1]
                if (attackLeftSquare.pieceColor == Color.BLACK){
                    val move = Move(startSquare = pawnSquare, endSquare = attackLeftSquare,
                        pieceType = PieceType.PAWN, capturedPiece = attackLeftSquare.pieceType)
                    validMoves.add(move)
                }
                if (attackLeftSquare == enPassantSquare) {
                    val move = Move(startSquare = pawnSquare, endSquare = attackLeftSquare,
                        pieceType = PieceType.PAWN, enPassantSquare = attackLeftSquare,
                    capturedPiece = PieceType.PAWN)
                    validMoves.add(move)
                }
            }
        }
        else if (ownColor == Color.BLACK) {
            if ((pawnSquare.piece.pinned == PinnedState.NONE ||
                pawnSquare.piece.pinned == PinnedState.DIAGONALA1H8) && col!= 0
            ) {
                val attackLeftSquare = board[col-1][row+1]
                if (attackLeftSquare.pieceColor == Color.WHITE){
                    val move = Move(startSquare = pawnSquare, endSquare = attackLeftSquare,
                        pieceType = PieceType.PAWN, capturedPiece = attackLeftSquare.pieceType)
                    validMoves.add(move)
                }
                if (attackLeftSquare == enPassantSquare){
                    val move = Move(startSquare = pawnSquare, endSquare = attackLeftSquare,
                        pieceType = PieceType.PAWN,enPassantSquare = attackLeftSquare,
                    capturedPiece = PieceType.PAWN)
                    validMoves.add(move)
                }
            }
            if ((pawnSquare.piece.pinned == PinnedState.NONE ||
                pawnSquare.piece.pinned == PinnedState.DIAGONALA8H1) && col!=7
            ) {
                val rightAttackSquare = board[col+1][row+1]
                if (rightAttackSquare.pieceColor == Color.WHITE){
                    val move = Move(startSquare = pawnSquare, endSquare = rightAttackSquare,
                        pieceType = PieceType.PAWN, capturedPiece = rightAttackSquare.pieceType)
                    validMoves.add(move)
                }
                if (rightAttackSquare == enPassantSquare){
                    val move = Move(startSquare = pawnSquare, endSquare = rightAttackSquare,
                        pieceType = PieceType.PAWN, enPassantSquare = rightAttackSquare,
                    capturedPiece = PieceType.PAWN)
                    validMoves.add(move)
                }
            }
        }
        return validMoves
    }

    /**
     * Generates valid moves for bishop (or queen) on square
     *
     */
    fun generateBishopMoves(bishopSquare: Square): MutableList<Move> {
        val validMoves = mutableListOf<Move>()
        val col = bishopSquare.col
        val row = bishopSquare.row
        val ownColor = bishopSquare.pieceColor
        val opponentColor = if (ownColor == Color.WHITE){
            Color.BLACK
        } else {
            Color.WHITE
        }

        val pinState = bishopSquare.piece.pinned

        if (pinState == PinnedState.DIAGONALA8H1 || pinState == PinnedState.NONE) {
            var squaresMoved = 1

            // up right moves
            while (col + squaresMoved <= 7 && row + squaresMoved <= 7) {
                val targetSquare = board[col + squaresMoved][row + squaresMoved]
                if (targetSquare.pieceColor == opponentColor){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                         pieceType = PieceType.BISHOP,
                        capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.pieceColor == ownColor){
                    break
                }
                else if (targetSquare.pieceType == PieceType.NONE){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        pieceType = PieceType.BISHOP, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }
            //down left moves
            squaresMoved = 1
            while (col - squaresMoved >= 0 && row -squaresMoved >= 0) {
                val targetSquare = board[col - squaresMoved][row - squaresMoved]
                if (targetSquare.pieceColor == opponentColor){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        pieceType = PieceType.BISHOP, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.pieceColor == ownColor){
                    break
                }
                else if (targetSquare.pieceType == PieceType.NONE){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        pieceType = PieceType.BISHOP, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }
        }
        if (pinState == PinnedState.DIAGONALA1H8 || pinState == PinnedState.NONE) {
            var squaresMoved = 1

            // up left moves
            while (col - squaresMoved >= 0 && row + squaresMoved <= 7) {
                val targetSquare = board[col - squaresMoved][row + squaresMoved]
                if (targetSquare.pieceColor == opponentColor){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        pieceType = PieceType.BISHOP, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.pieceColor == ownColor){
                    break
                }
                else if (targetSquare.pieceType == PieceType.NONE){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        pieceType = PieceType.BISHOP, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }
            squaresMoved = 1
            // down right moves
            while (col + squaresMoved <= 7 && row - squaresMoved >= 0) {
                val targetSquare = board[col + squaresMoved][row - squaresMoved]
                if (targetSquare.pieceColor== opponentColor){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        pieceType = PieceType.BISHOP, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.pieceColor == ownColor){
                    break
                }
                else if (targetSquare.pieceType == PieceType.NONE){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        pieceType = PieceType.BISHOP, capturedPiece = targetSquare.pieceType)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }
        }
        return validMoves
    }

    /**
     * generates all legal moves for queen on given square
     * @see generateBishopMoves
     * @see generateRookMoves
     */
    fun generateQueenMoves(queenSquare: Square): MutableList<Move> {
        return (generateRookMoves(queenSquare) + generateBishopMoves(queenSquare)) as MutableList<Move>
    }

    /**
     * generates all legal moves for king on given square
     */
    fun generateKingMoves(kingSquare: Square): MutableList<Move> {
        if (kingSquare.pieceType != PieceType.KING){
            Log.wtf("WTF","wrong piece type")
        }
        val validMoves = mutableListOf<Move>()
        val col = kingSquare.col
        val row = kingSquare.row
        val ownColor = kingSquare.pieceColor
        val opponentColor = if (ownColor == Color.WHITE){
            Color.BLACK
        } else {
            Color.WHITE
        }
        val kingMoves = listOf(
            Pair(-1,-1), Pair(-1, 0), Pair(-1,1), Pair(0, 1),
            Pair(1,1), Pair(1, 0),Pair(1,-1), Pair(0, -1)
        )
        for (move in kingMoves){
            if (col + move.first in 0..7 && row + move.second in 0..7) {
                val targetSquare = board[col + move.first][row + move.second]
                if (targetSquare.pieceColor != ownColor && !isKingInCheck(targetSquare,ownColor)) {
                    val newMove = Move(
                        startSquare = kingSquare, endSquare = targetSquare,
                        pieceType = PieceType.KING, capturedPiece = targetSquare.pieceType)
                    validMoves.add(newMove)
                }
            }
        }

        //castling moves
        if (ownColor == Color.BLACK){
            if (blackCastleKingRights
                && getSquare("f8").pieceType == PieceType.NONE
                && getSquare("g8").pieceType == PieceType.NONE
                && !isKingInCheck(getSquare("f8"),ownColor)
                && !isKingInCheck(getSquare("g8"),ownColor)

            ){
                val move = Move(startSquare = kingSquare, endSquare = getSquare("g8"),
                    pieceType = PieceType.KING, longNotation = "o-o",
                castling = Castleing.BLACK_KING, capturedPiece = PieceType.NONE)
                validMoves.add(move)

            }
            if (blackCastleQueenRights
                && getSquare("d8").pieceType == PieceType.NONE
                && getSquare("c8").pieceType == PieceType.NONE
                && getSquare("b8").pieceType == PieceType.NONE
                && !isKingInCheck(getSquare("d8"),ownColor)
                && !isKingInCheck(getSquare("c8"),ownColor)

            ){
                val move = Move(startSquare = kingSquare, endSquare = getSquare("c8"),
                    pieceType = PieceType.KING, longNotation = "o-o-o",
                    castling = Castleing.BLACK_QUEEN, capturedPiece = PieceType.NONE)
                validMoves.add(move)

            }
        }
        else if (ownColor == Color.WHITE){
            if (whiteCastleKingRights
                && getSquare("f1").pieceType == PieceType.NONE
                && getSquare("g1").pieceType == PieceType.NONE
                && !isKingInCheck(getSquare("f1"),ownColor)
                && !isKingInCheck(getSquare("g1"),ownColor)

            ){
                val move = Move(startSquare = kingSquare, endSquare = getSquare("g1"),
                    pieceType = PieceType.KING, longNotation = "O-O",
                    castling = Castleing.WHITE_KING,
                    capturedPiece = PieceType.NONE)
                validMoves.add(move)

            }
            if (whiteCastleQueenRights
                && getSquare("d1").pieceType == PieceType.NONE
                && getSquare("c1").pieceType == PieceType.NONE
                && getSquare("b1").pieceType == PieceType.NONE
                && !isKingInCheck(getSquare("d1"),ownColor)
                && !isKingInCheck(getSquare("c1"),ownColor)

            ){
                val move = Move(startSquare = kingSquare, endSquare = getSquare("c1"),
                    pieceType = PieceType.KING, longNotation = "O-O-O",
                    castling = Castleing.WHITE_QUEEN,
                    capturedPiece = PieceType.NONE)
                validMoves.add(move)
            }
        }
            return validMoves
    }


    /**
     * checks whether the king on given square is in check
     *
     */
    fun isKingInCheck(kingSquare: Square,color: Color = Color.NONE): Boolean{

        val col = kingSquare.col
        val row = kingSquare.row

        val ownColor = if (color != Color.NONE){
            color
        }
        else {
            kingSquare.pieceColor
        }
        val opponentColor = if (ownColor == Color.WHITE){
            Color.BLACK
        }
        else {
            Color.WHITE
        }
        var targetSquare: Square
        //diagonals
        //up left
        var squaresChecked = 1
        while (col - squaresChecked >= 0 && row + squaresChecked <= 7) {
            targetSquare = board[col - squaresChecked][row + squaresChecked]

            if (targetSquare.pieceColor == Color.NONE){
                squaresChecked ++
                continue
            }
            else if (targetSquare.pieceColor == ownColor){
                break
            }
            else if (targetSquare.pieceColor == opponentColor){
                if (targetSquare.pieceType == PieceType.BISHOP ||
                    targetSquare.pieceType == PieceType.QUEEN ||
                    (targetSquare.pieceType == PieceType.KING &&
                            squaresChecked == 1)){

                    return true
                }
                break
            }
        }
        //up right
        squaresChecked = 1

        while (col + squaresChecked <= 7 && row + squaresChecked <= 7) {
            targetSquare = board[col + squaresChecked][row + squaresChecked]

            if (targetSquare.pieceColor == Color.NONE){
                squaresChecked ++
                continue
            }
            else if (targetSquare.pieceColor == ownColor){
                break
            }
            else if (targetSquare.pieceColor == opponentColor){
                if (targetSquare.pieceType == PieceType.BISHOP ||
                    targetSquare.pieceType == PieceType.QUEEN ||
                    (targetSquare.pieceType == PieceType.KING &&
                            squaresChecked == 1) ){

                    return true
                }
                break
            }
        }
        //down right
        squaresChecked = 1

        while (col + squaresChecked <= 7 && row - squaresChecked >=0) {
            targetSquare = board[col + squaresChecked][row - squaresChecked]

            if (targetSquare.pieceColor == Color.NONE){
                squaresChecked ++
                continue
            }
            else if (targetSquare.pieceColor == ownColor){
                break
            }
            else if (targetSquare.pieceColor == opponentColor){
                if (targetSquare.pieceType == PieceType.BISHOP ||
                    targetSquare.pieceType == PieceType.QUEEN ||
                    (targetSquare.pieceType == PieceType.KING &&
                            squaresChecked == 1)){

                    return true
                }
                break
            }
        }
        //down left
        squaresChecked = 1

        while (col - squaresChecked >= 0 && row - squaresChecked >= 0) {
            targetSquare = board[col - squaresChecked][row - squaresChecked]

            if (targetSquare.pieceColor == Color.NONE){
                squaresChecked ++
                continue
            }
            else if (targetSquare.pieceColor == ownColor){
                break
            }
            else if (targetSquare.pieceColor == opponentColor){
                if (targetSquare.pieceType == PieceType.BISHOP ||
                    targetSquare.pieceType == PieceType.QUEEN ||
                    (targetSquare.pieceType == PieceType.KING &&
                            squaresChecked == 1)){

                    return true
                }
                break
            }
        }

        //vertical
        //up
        squaresChecked = 1

        while (row + squaresChecked <= 7) {
            targetSquare = board[col][row + squaresChecked]
            if (targetSquare.pieceColor == Color.NONE){
                squaresChecked ++
                continue
            }
            else if (targetSquare.pieceColor == ownColor){
                break
            }
            else if (targetSquare.pieceColor == opponentColor){
                if (targetSquare.pieceType == PieceType.ROOK ||
                    targetSquare.pieceType == PieceType.QUEEN ||
                    (targetSquare.pieceType == PieceType.KING &&
                            squaresChecked == 1)){
                    return true
                }
                break
            }
        }
        //down
        squaresChecked = 1
        while (row - squaresChecked >= 0) {
            targetSquare = board[col][row - squaresChecked]
            if (targetSquare.pieceColor == Color.NONE){
                squaresChecked ++
                continue
            }
            else if (targetSquare.pieceColor == ownColor){
                break
            }
            else if (targetSquare.pieceColor == opponentColor){
                if (targetSquare.pieceType == PieceType.ROOK ||
                    targetSquare.pieceType == PieceType.QUEEN ||
                    (targetSquare.pieceType == PieceType.KING &&
                            squaresChecked == 1)){
                    return true
                }
                break
            }
        }
        //right
        squaresChecked = 1
        while (col + squaresChecked <=7 ) {
            targetSquare = board[col + squaresChecked][row]
            if (targetSquare.pieceColor == Color.NONE){
                squaresChecked ++
                continue
            }
            else if (targetSquare.pieceColor == ownColor){
                break
            }
            else if (targetSquare.pieceColor == opponentColor){
                if (targetSquare.pieceType == PieceType.ROOK ||
                    targetSquare.pieceType == PieceType.QUEEN ||
                    (targetSquare.pieceType == PieceType.KING &&
                            squaresChecked == 1)){
                    return true
                }
                break
            }
        }
        //left
        squaresChecked = 1
        while (col - squaresChecked >= 0 ) {
            targetSquare = board[col - squaresChecked][row]
            if (targetSquare.pieceColor == Color.NONE){
                squaresChecked ++
                continue
            }
            else if (targetSquare.pieceColor == ownColor){
                break
            }
            else if (targetSquare.pieceColor == opponentColor){
                if (targetSquare.pieceType == PieceType.ROOK ||
                    targetSquare.pieceType == PieceType.QUEEN ||
                    (targetSquare.pieceType == PieceType.KING &&
                            squaresChecked == 1)){
                    return true
                }
                break
            }
        }

        //pawns

        if (ownColor == Color.WHITE) {
            if (col > 0 && row > 0 && board[col - 1][row - 1].pieceType == PieceType.PAWN
                && board[col - 1][row - 1].pieceColor == Color.BLACK)
            {

                return true
            }
            else if (col < 7 && row > 0 && board[col + 1][row - 1].pieceType == PieceType.PAWN
                && board[col + 1][row - 1].pieceColor == Color.BLACK)
            {
                return true
            }
        }
        else if (ownColor == Color.BLACK) {
            if (col > 0 &&  row < 7 && board[col - 1][row + 1].pieceType == PieceType.PAWN
                && board[col - 1][row + 1].pieceColor == Color.WHITE) {
                return true
            }
            else if (col < 7 &&  row < 7 && board[col + 1][row + 1].pieceType == PieceType.PAWN
                && board[col + 1][row + 1].pieceColor == Color.WHITE
            ) {
                return true
            }
        }

        //knights
        val knightMoves = listOf(
            Pair(-2,-1), Pair(-2, +1),
            Pair(+2, -1), Pair(+2, +1),
            Pair(-1, +2), Pair(-1, -2),
            Pair(+1, -2), Pair(+1, +2)
        )
        for (move in knightMoves) {
            val targetCol = kingSquare.col + move.first
            val targetRow = kingSquare.row + move.second
            if (targetCol in 0..7
                && targetRow in 0..7) {
                val targetSquare = board[targetCol][targetRow]
                if (targetSquare.pieceType == PieceType.KNIGHT
                    && targetSquare.pieceColor == opponentColor) {
                    return true

                }
            }
        }

    return false
    }

    fun promotePawn(square: Square, pieceType: PieceType, color: Color){
        board[square.col][square.row].piece = Piece(color,pieceType)

        if (color == Color.WHITE){
            checkForPins(blackKingSquare)
            isKingInCheck(blackKingSquare)
        }
        else {
            checkForPins(whiteKingSquare)
            isKingInCheck(whiteKingSquare)
        }

    }

    /**
     * Loads a position onto the board from given FEN string
     *
     * see https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     *
     * @param fenString the FEN to load
     */
    fun loadPositionFenString(fenString: String){
        clearBoard()
        val typeHashMap = hashMapOf(
            "k" to PieceType.KING,
            "q" to PieceType.QUEEN,
            "r" to PieceType.ROOK,
            "b" to PieceType.BISHOP,
            "n" to PieceType.KNIGHT,
            "p" to PieceType.PAWN
        )

        var col = 0
        var row = 0
        val fen = fenString.split(' ')[0]

        for (character in fen){
            if (character == '/') {
                col = 0
                row ++

            }
            else if (character.isDigit()) {
                col += character.digitToInt()
            }
            else {
                val color = if (character.isUpperCase()){
                    Color.WHITE
                    } else {
                        Color.BLACK
                }
                val type = typeHashMap[character.lowercase()]
                board[col][row].piece = Piece(color,type!!)
                if (character == 'k') {
                    blackKingSquare = board[col][row]
                }
                if (character == 'K') {
                    whiteKingSquare = board[col][row]

                }
                col++
            }
        }
        val castlingRights =
            if (fenString.contains('w')) {
                fenString.substringAfterLast('w')
        }
            else {
                fenString.substringAfterLast('b')
        }

        if (castlingRights.contains('K')) {
            whiteCastleKingRights = true
            }
        if (castlingRights.contains('Q')) {
            whiteCastleQueenRights = true
        }
        if (castlingRights.contains('k')) {
            blackCastleKingRights = true
        }
        if (castlingRights.contains('q')) {
            blackCastleQueenRights = true
        }

        checkForPins(blackKingSquare)
        checkForPins(whiteKingSquare)

        val fenList = fenString.split(" ")
        val passantString = fenList[3]

        turn = if (fenString.contains("w")){
            Color.WHITE
        } else {
            Color.BLACK
        }
        enPassantSquare = if (passantString != "-") {
            getSquare(passantString)
        } else {
            null
        }
    }

    /**
     *  Generates the partial FEN string for the current position on board
     *  generates the FEN minus turn counters, which is important for using to determine
     *  3 move repetitions
     *
     * see https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     */
    fun getPartialFenStringFromPosition(): String {
        val typeHashMap = hashMapOf(
            PieceType.KING to "k",
            PieceType.QUEEN to "q",
            PieceType.ROOK to "r",
            PieceType.BISHOP to "b",
            PieceType.KNIGHT to "n",
            PieceType.PAWN to "p"
        )
        var fenString = ""

        for (row in 0..7){
            var emptySquares = 0
            for (col in 0..7) {
                val square = board[col][row]
                if (square.pieceType != PieceType.NONE) {
                    val piece = square.pieceType
                    var char = typeHashMap[piece]
                    if (square.pieceColor == Color.WHITE){
                        char = char!!.uppercase()
                    }
                    if (emptySquares != 0){
                        fenString += emptySquares.toString()
                        emptySquares = 0
                    }
                    fenString += char
                }
                else if (square.pieceType == PieceType.NONE){
                    emptySquares ++
                }
                if (col == 7){
                    if (emptySquares != 0){
                        fenString += emptySquares.toString()
                    }
                    if (row != 7) {
                        fenString += "/"
                    }
                }
            }
        }
        if (turn == Color.WHITE){
            fenString += " w "
        }
        else if (turn == Color.BLACK){
            fenString += " b "
        }

        var castlingString = ""
        if (whiteCastleKingRights){
            castlingString += "K"
        }
        if (whiteCastleQueenRights){
            castlingString += "Q"
        }
        if (blackCastleKingRights){
            castlingString += "k"
        }
        if (blackCastleQueenRights){
            castlingString += "q"
        }

        fenString += if (castlingString != ""){
            castlingString
        } else {
            "-"
        }
        val colMap = hashMapOf(
            0 to "a",
            1 to "b",
            2 to "c",
            3 to "d",
            4 to "e",
            5 to "f",
            6 to "g",
            7 to "h",
        )
        var enPassantString = " "
        if (enPassantSquare != null) {

            enPassantString += colMap[enPassantSquare!!.col]
            enPassantString += 7 - (enPassantSquare!!.row) + 1
            enPassantString += " "
        }
        fenString += if (enPassantString == " ") {
            " - "
        }
        else {
            enPassantString
        }


        return fenString

    }

    /**
     * moves king and rook to correct squares for castling
     */
    private fun doCastleMove(move: Move){
        when (move.castling)  {
            Castleing.BLACK_KING -> {
                board[7][0].piece = Piece(Color.NONE,PieceType.NONE)
                board[5][0].piece = Piece(Color.BLACK,PieceType.ROOK)
                blackCastleKingRights = false
                blackCastleQueenRights = false
            }
            Castleing.BLACK_QUEEN -> {
                board[0][0].piece = Piece(Color.NONE,PieceType.NONE)
                board[3][0].piece = Piece(Color.BLACK,PieceType.ROOK)
                blackCastleKingRights = false
                blackCastleQueenRights = false

            }
            Castleing.WHITE_KING -> {
                board[7][7].piece = Piece(Color.NONE,PieceType.NONE)
                board[5][7].piece = Piece(Color.WHITE,PieceType.ROOK)
                whiteCastleKingRights = false
                whiteCastleQueenRights = false
            }
            Castleing.WHITE_QUEEN -> {
                board[0][7].piece = Piece(Color.NONE,PieceType.NONE)
                board[3][7].piece = Piece(Color.WHITE,PieceType.ROOK)
                whiteCastleKingRights = false
                whiteCastleQueenRights = false

            }
            else -> {}
        }
    }

    /**
     * Removed the captured pawn in an en passant move
     */
    private fun doEnPassantMove(move: Move){
        if (turn == Color.WHITE){

            board[move.endSquare.col][move.endSquare.row +1].piece = Piece(Color.NONE,PieceType.NONE)
        }
        else {

            board[move.endSquare.col][move.endSquare.row -1].piece = Piece(Color.NONE,PieceType.NONE)
        }
    }

    /**
     * increments hashmap for current position and sets draw if three move repetition has happened
     */
    private fun checkThreefoldRepetition(){
        val fenString = getPartialFenStringFromPosition()
        positionMap[fenString] = (positionMap[fenString] ?:0) +1
        if (positionMap.containsValue(3)){
            result.postValue(GameResult.DRAW_BY_REPETITION)
        }
    }

    private fun updateKingPosition(move: Move){
        if (turn == Color.WHITE){
            whiteKingSquare = move.endSquare
        }
        else if (turn == Color.BLACK) {
            blackKingSquare = move.endSquare
        }
    }

    fun changeTurn(){
        if (turn == Color.WHITE){
            turn = Color.BLACK
        }
        else {
            turn = Color.WHITE
        }
    }
}

