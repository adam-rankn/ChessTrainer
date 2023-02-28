package com.pinguapps.chesstrainer.data

import kotlin.math.abs
import kotlin.math.min

class Chessboard {

    val board = Array(8) { row -> Array(8) { col -> Square(col,row) } }

    var whiteCastleQueenRights = true
    var whiteCastleKingRights  = true
    var blackCastleQueenRights = true
    var blackCastleKingRights  = true
    var enPassantSquare: Square? = null

    var whiteKingSquare = getSquare("e1")
    var blackKingSquare = getSquare("e8")

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

        val row = rowStr.toInt() - 1
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

    fun getPieceOnSquare(notation: String): Piece {
        return getSquare(notation).piece
    }

    fun makeMove(move: Move){

        val startSquare = move.startSquare
        val endSquare = move.endSquare
        val startCol = startSquare.col
        val startRow = startSquare.row
        val piece = board[startCol][startRow].piece
        val endCol = endSquare.col
        val endRow = endSquare.row

        board[startCol][startRow].piece = Piece(Color.NONE, PieceType.NONE)
        board[endCol][endRow].piece = piece
        placePiece(endSquare, piece.color, piece.type)

    }

    fun makeMoveFromString(startStr: String, endStr: String){
        val startSquare = getSquare(startStr)
        val endSquare = getSquare(endStr)
        val startCol = startSquare.col
        val startRow = startSquare.row
        val piece = board[startCol][startRow].piece
        val endCol = endSquare.col
        val endRow = endSquare.row

        board[startCol][startRow].piece = Piece(Color.NONE, PieceType.NONE)
        board[endCol][endRow].piece = piece
        placePiece(endSquare, piece.color, piece.type)
    }

    fun generatePieceMoves(square: Square): MutableList<Move> {
        val pieceType = square.piece.type
        if(pieceType == PieceType.ROOK) {
            return generateRookMoves(square)
        }
        else return mutableListOf()
    }

    fun placePiece(square: Square, color: Color, type: PieceType) {
        square.piece = Piece(color,type)
    }

    fun isClearVerticallyBetween(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val squaresBetween = abs(from.row - to.row) - 1
        if (squaresBetween == 0 ) return true
        for (i in 1..squaresBetween) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceTypeOnSquare(to.col,nextRow) != PieceType.NONE) {
                return false
            }
        }
        return true
    }

    fun isClearHorizontallyBetween(from: Square, to: Square): Boolean {
        if (from.row != to.row) return false
        val squaresBetween = abs(from.col - to.col) - 1
        if (squaresBetween== 0 ) return true
        for (i in 1..squaresBetween) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            if (pieceTypeOnSquare(nextCol,from.row) != PieceType.NONE) {
                return false
            }
        }
        return true
    }

    fun isClearDiagonally(from: Square, to: Square): Boolean {
        val squaresBetween = abs(from.col - to.col) - 1
        for (i in 1..squaresBetween) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceTypeOnSquare(nextCol,nextRow) != PieceType.NONE) {
                return false
            }
        }
        return true
    }

    fun canRookMove(from: Square, to: Square): Boolean {
        if (from.piece.color == to.piece.color) {
            return false
        }
        else if (from.col == to.col && isClearVerticallyBetween(from, to) ||
            from.row == to.row && isClearHorizontallyBetween(from, to)) {
            return true
        }
        return false
    }

    fun generateRookMoves(fromSquare: Square): MutableList<Move> {
        val validMoves = mutableListOf<Move>()
        val row = fromSquare.row
        val col = fromSquare.col
        val movesUp = 7-row
        val pinned = fromSquare.piece.pinned

        if (pinned == PinnedState.NONE || pinned == PinnedState.VERTICAL) {
            for (i in 1..movesUp) {
                val toSquare = board[col][row + i]
                if (canRookMove(fromSquare, toSquare)) {
                    val isCapture = fromSquare.piece.color != Color.NONE
                    val move = Move(fromSquare, toSquare, PieceType.ROOK, isCapture)
                    validMoves.add(move)
                }
            }

            for (i in 1..row) {
                if (canRookMove(fromSquare, board[col][row - i])) {
                    val isCapture = fromSquare.piece.color != Color.NONE
                    val move = Move(fromSquare, board[col][row - i], PieceType.ROOK, isCapture)
                    validMoves.add(move)
                }
            }
        }

        val movesRight = 7-col

        if (pinned == PinnedState.NONE || pinned == PinnedState.HORIZONTAL) {
            for (i in 1..movesRight) {
                if (canRookMove(fromSquare, board[col + i][row])) {
                    val isCapture = fromSquare.piece.color != Color.NONE
                    val move = Move(fromSquare, board[col + i][row], PieceType.ROOK, isCapture)
                    validMoves.add(move)
                }
            }

            for (i in 1..col) {
                if (canRookMove(fromSquare, board[col - i][row])) {
                    val isCapture = fromSquare.piece.color != Color.NONE
                    val move = Move(fromSquare, board[col - i][row], PieceType.ROOK, isCapture)
                    validMoves.add(move)
                }
            }
        }
        //todo use bishop moves method for optimization
        return validMoves
    }

    fun checkForPins(kingSquare: Square) {
        //todo use actual king piece, track king locations on board
        val ownColor = kingSquare.piece.color
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
                potentialPinnedPiece.pinned = PinnedState.DIAGONALA1H8

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
                potentialPinnedPiece.pinned = PinnedState.DIAGONALA8H1
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
                potentialPinnedPiece.pinned = PinnedState.DIAGONALA1H8
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
                potentialPinnedPiece.pinned = PinnedState.DIAGONALA8H1
            }
        }
    }

    fun pieceTypeOnSquare(col: Int, row: Int): PieceType {
        return board[col][row].piece.type
    }

    fun generateValidKnightMoves(knightSquare: Square): MutableList<Move> {
        val col = knightSquare.col
        val row = knightSquare.row
        val ownColor = knightSquare.piece.color
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
                    if (targetSquare.piece.color != ownColor) {
                        val isCapture = (targetSquare.piece.color == opponentColor)
                        val move = Move(startSquare = knightSquare, endSquare = targetSquare,
                            isCapture = isCapture, piece = PieceType.KNIGHT)
                        validMoves.add(move)
                    }
                }
            }
        }
        return validMoves
    }

    fun generateValidPawnMoves(pawnSquare: Square): MutableList<Move> {
        val validMoves = mutableListOf<Move>()
        val pinState = pawnSquare.piece.pinned
        when (pinState) {
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

    private fun generatePawnPushMoves(pawnSquare: Square): MutableList<Move> {
        val col = pawnSquare.col
        val row = pawnSquare.row
        val ownColor = pawnSquare.piece.color
        val validMoves = mutableListOf<Move>()
        if (pawnSquare.row == 1 && ownColor == Color.WHITE
            && board[col][row+1].piece.type == PieceType.NONE
            && board[col][row+2].piece.type == PieceType.NONE){
            val move = Move(startSquare = pawnSquare, endSquare = board[col][row+2],
                isCapture = false, piece = PieceType.PAWN)
            validMoves.add(move)
        }
        else if (pawnSquare.row == 6 && ownColor == Color.BLACK
            && board[col][row-1].piece.type == PieceType.NONE
            && board[col][row-2].piece.type == PieceType.NONE) {
            val move = Move(startSquare = pawnSquare, endSquare = board[col][row-2],
                isCapture = false, piece = PieceType.PAWN)
            validMoves.add(move)
        }
        if (ownColor == Color.WHITE && board[col][row+1].piece.type == PieceType.NONE){
            val move = Move(startSquare = pawnSquare, endSquare = board[col][row+1],
                isCapture = false, piece = PieceType.PAWN)
            validMoves.add(move)
        }
        else if (ownColor == Color.BLACK && board[col][row-1].piece.type == PieceType.NONE) {
            val move = Move(startSquare = pawnSquare, endSquare = board[col][row-1],
                isCapture = false, piece = PieceType.PAWN)
            validMoves.add(move)
        }
        return  validMoves
    }

    private fun generatePawnCaptures(pawnSquare: Square): MutableList<Move> {
        val col = pawnSquare.col
        val row = pawnSquare.row
        val ownColor = pawnSquare.piece.color
        val validMoves = mutableListOf<Move>()

        if (ownColor == Color.WHITE) {
            if (pawnSquare.piece.pinned == PinnedState.NONE ||
                pawnSquare.piece.pinned == PinnedState.DIAGONALA1H8
            ) {
                if (col != 7 && board[col+1][row+1].piece.color == Color.BLACK){
                    val move = Move(startSquare = pawnSquare, endSquare = board[col+1][row+1],
                        isCapture = true, piece = PieceType.PAWN)
                    validMoves.add(move)
                }
                if (col != 0 && board[col+1][row+1] == enPassantSquare) {
                    val move = Move(startSquare = pawnSquare, endSquare = board[col+1][row+1],
                        isCapture = true, piece = PieceType.PAWN)
                    board[col+1][row].piece = Piece(Color.NONE,PieceType.NONE)
                    validMoves.add(move)
                }
            }
            if (pawnSquare.piece.pinned == PinnedState.NONE ||
                pawnSquare.piece.pinned == PinnedState.DIAGONALA8H1
            ) {
                if (col != 0 && board[col-1][row+1].piece.color == Color.BLACK){
                    val move = Move(startSquare = pawnSquare, endSquare = board[col-1][row+1],
                        isCapture = true, piece = PieceType.PAWN)
                    validMoves.add(move)
                }
                if (col != 0 && board[col-1][row+1] == enPassantSquare) {
                    val move = Move(startSquare = pawnSquare, endSquare = board[col-1][row+1],
                        isCapture = true, piece = PieceType.PAWN)
                    board[col-1][row].piece = Piece(Color.NONE,PieceType.NONE)
                    validMoves.add(move)
                }
            }
        }
        else if (ownColor == Color.BLACK) {
            if (pawnSquare.piece.pinned == PinnedState.NONE ||
                pawnSquare.piece.pinned == PinnedState.DIAGONALA1H8
            ) {
                if (col != 0 && board[col-1][row-1].piece.color == Color.WHITE){
                    val move = Move(startSquare = pawnSquare, endSquare = board[col-1][row-1],
                        isCapture = true, piece = PieceType.PAWN)
                    validMoves.add(move)
                }
                if (col != 0 && board[col-1][row-1] == enPassantSquare){
                    val move = Move(startSquare = pawnSquare, endSquare = board[col-1][row-1],
                        isCapture = true, piece = PieceType.PAWN)
                    board[col-1][row].piece = Piece(Color.NONE,PieceType.NONE)
                    validMoves.add(move)
                }
            }
            if (pawnSquare.piece.pinned == PinnedState.NONE ||
                pawnSquare.piece.pinned == PinnedState.DIAGONALA8H1
            ) {
                if (col != 7 && board[col+1][row-1].piece.color == Color.WHITE){
                    val move = Move(startSquare = pawnSquare, endSquare = board[col+1][row-1],
                        isCapture = true, piece = PieceType.PAWN)
                    validMoves.add(move)
                }
                if (col != 7 && board[col+1][row-1] == enPassantSquare){
                    val move = Move(startSquare = pawnSquare, endSquare = board[col+1][row-1],
                        isCapture = true, piece = PieceType.PAWN)
                    board[col+1][row].piece = Piece(Color.NONE,PieceType.NONE)
                    validMoves.add(move)
                }
            }
        }
        return validMoves
    }

    fun generateBishopMoves(bishopSquare: Square): MutableList<Move> {
        val validMoves = mutableListOf<Move>()
        val col = bishopSquare.col
        val row = bishopSquare.row
        val ownColor = bishopSquare.piece.color
        val opponentColor = if (ownColor == Color.WHITE){
            Color.BLACK
        } else {
            Color.WHITE
        }

        val pinState = bishopSquare.piece.pinned

        if (pinState == PinnedState.DIAGONALA1H8 || pinState == PinnedState.NONE) {
            var squaresMoved = 1

            // up right moves
            while (col + squaresMoved <= 7 && row + squaresMoved <= 7) {
                val targetSquare = board[col + squaresMoved][row + squaresMoved]
                if (targetSquare.piece.color == opponentColor){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        isCapture = true, piece = PieceType.BISHOP)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.piece.color == ownColor){
                    break
                }
                else if (targetSquare.piece.type == PieceType.NONE){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        isCapture = false, piece = PieceType.BISHOP)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }
            //down left moves
            squaresMoved = 1
            while (col - squaresMoved >= 0 && row -squaresMoved >= 0) {
                val targetSquare = board[col - squaresMoved][row - squaresMoved]
                if (targetSquare.piece.color == opponentColor){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        isCapture = true, piece = PieceType.BISHOP)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.piece.color == ownColor){
                    break
                }
                else if (targetSquare.piece.type == PieceType.NONE){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        isCapture = false, piece = PieceType.BISHOP)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }
        }
        if (pinState == PinnedState.DIAGONALA8H1 || pinState == PinnedState.NONE) {
            var squaresMoved = 1

            // up left moves
            while (col - squaresMoved >= 0 && row + squaresMoved <= 7) {
                val targetSquare = board[col - squaresMoved][row + squaresMoved]
                if (targetSquare.piece.color == opponentColor){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        isCapture = true, piece = PieceType.BISHOP)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.piece.color == ownColor){
                    break
                }
                else if (targetSquare.piece.type == PieceType.NONE){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        isCapture = false, piece = PieceType.BISHOP)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }
            squaresMoved = 1
            // down right moves
            while (col + squaresMoved <= 7 && row - squaresMoved >= 0) {
                val targetSquare = board[col + squaresMoved][row - squaresMoved]
                if (targetSquare.piece.color == opponentColor){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        isCapture = true, piece = PieceType.BISHOP)
                    validMoves.add(move)
                    break
                }
                else if (targetSquare.piece.color == ownColor){
                    break
                }
                else if (targetSquare.piece.type == PieceType.NONE){
                    val move = Move(startSquare = bishopSquare, endSquare = targetSquare,
                        isCapture = false, piece = PieceType.BISHOP)
                    validMoves.add(move)
                    squaresMoved ++
                }
            }
        }
        return validMoves
    }

    fun generateQueenMoves(queenSquare: Square): MutableList<Move> {
        return (generateRookMoves(queenSquare) + generateBishopMoves(queenSquare)) as MutableList<Move>
    }

    fun generateKingMoves(kingSquare: Square): MutableList<Move> {
        val validMoves = mutableListOf<Move>()
        val col = kingSquare.col
        val row = kingSquare.row
        val ownColor = kingSquare.piece.color
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
            val targetSquare = board[col + move.first][row + move.second]
            if (targetSquare.piece.color != ownColor && !isKingInCheck(targetSquare)){
                val isCapture = targetSquare.piece.color == opponentColor
                val move = Move(startSquare = kingSquare, endSquare = targetSquare,
                    isCapture = isCapture, piece = PieceType.KING)
                validMoves.add(move)
            }
        }

        //castling moves
        if (ownColor == Color.BLACK){
            if (blackCastleKingRights && !isKingInCheck(getSquare("f8"))
                && !isKingInCheck(getSquare("g8"))
                && getSquare("f8").piece.type == PieceType.NONE
                && getSquare("g8").piece.type == PieceType.NONE
            ){
                val move = Move(startSquare = kingSquare, endSquare = getSquare("g8"),
                    isCapture = false, piece = PieceType.KING, notation = "o-o")
                validMoves.add(move)

            }
            if (blackCastleQueenRights && !isKingInCheck(getSquare("d8"))
                && !isKingInCheck(getSquare("c8"))
                && getSquare("d8").piece.type == PieceType.NONE
                && getSquare("c8").piece.type == PieceType.NONE
            ){
                val move = Move(startSquare = kingSquare, endSquare = getSquare("c8"),
                    isCapture = false, piece = PieceType.KING, notation = "o-o-o")
                validMoves.add(move)

            }
        }
        else if (ownColor == Color.WHITE){
            if (whiteCastleKingRights && !isKingInCheck(getSquare("f1"))
                && !isKingInCheck(getSquare("g1"))
                && getSquare("f1").piece.type == PieceType.NONE
                && getSquare("g1").piece.type == PieceType.NONE
            ){
                val move = Move(startSquare = kingSquare, endSquare = getSquare("g1"),
                    isCapture = false, piece = PieceType.KING, notation = "O-O")
                validMoves.add(move)

            }
            if (whiteCastleQueenRights && !isKingInCheck(getSquare("d1"))
                && !isKingInCheck(getSquare("c1"))
                && getSquare("d1").piece.type == PieceType.NONE
                && getSquare("c1").piece.type == PieceType.NONE
            ){
                val move = Move(startSquare = kingSquare, endSquare = getSquare("c1"),
                    isCapture = false, piece = PieceType.KING, notation = "O-O-O")
                validMoves.add(move)
            }
        }
            return validMoves
    }

    fun isKingInCheck(square: Square): Boolean{
    return false

    }

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
        var row = 7
        val fen = fenString.split(' ')[0]

        for (character in fen){
            if (character == '/') {
                col = 0
                row --

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
    }
    //todo en passant
    //todo 50 move counter/half moves
    //todo full move ctr
}

