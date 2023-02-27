package com.pinguapps.chesstrainer.data

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.min

class Chessboard() {

    val board = Array(8) { row -> Array(8) { col -> Square(col,row) } }

    var whiteCastleQueenRights = true
    var whiteCastleKingRights  = true
    var blackCastleQueenRights = true
    var blackCastleKingRights  = true

    init {
        loadPositionFenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
        var whiteCastleQueenRights = true
        var whiteCastleKingRights  = true
        var blackCastleQueenRights = true
        var blackCastleKingRights  = true
    }

    fun clearBoard() {
        for (col in board){
            for (square in col){
                square.piece = Piece(Color.NONE,PieceType.NONE)
            }
        }
        var whiteCastleQueenRights = false
        var whiteCastleKingRights  = false
        var blackCastleQueenRights = false
        var blackCastleKingRights  = false
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

    fun isMoveValid(start: String, end: String): Boolean {
        val piece = getSquare(start)
        val playerColor = piece.color
        val targetSquare = getSquare(end)

        if (targetSquare.piece.color == piece.color){
            return false
        }

        return false
    }



    fun getValidMoves(start: String){
        val square = getSquare(start)
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

    fun canBishopMove(from: Square, to: Square): Boolean {
        if (from.piece.color == to.piece.color) {
            return false
        }
        // check if on same diagonal
        else if (abs(from.col - to.col) == abs(from.row - to.row)) {
            return isClearDiagonally(from, to)
        }
        return false
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

        for (i in 1..movesUp){
            val toSquare = board[col][row+i]
            if (canRookMove(fromSquare,toSquare)){
                val isCapture = fromSquare.piece.color != Color.NONE
                val move = Move(fromSquare,toSquare,PieceType.ROOK, isCapture)
                validMoves.add(move)
            }
        }

        //todo same thing
        for (i in 1..row){
            if (canRookMove(fromSquare,board[col][row-i])){
                val move = Move(fromSquare,board[col][row-i],PieceType.ROOK)
                validMoves.add(move)
            }
        }

        val movesRight = 7-col

        for (i in 1..movesRight){
            if (canRookMove(fromSquare,board[col+i][row])){
                val move = Move(fromSquare,board[col+i][row],PieceType.ROOK)
                validMoves.add(move)
            }
        }

        for (i in 1..col){
            if (canRookMove(fromSquare,board[col-i][row])){
                val move = Move(fromSquare,board[col-i][row],PieceType.ROOK)
                validMoves.add(move)
            }
        }
        return validMoves
    }

    fun checkForPins(kingSquare: Square): Boolean {
        val ownColor = kingSquare.piece.color
        val opponentColor = if (ownColor == Color.WHITE){
            Color.BLACK
        } else {
            Color.WHITE
        }
        val row = kingSquare.row
        val col = kingSquare.col
        val movesUp = 7-row
        for (i in 1..movesUp){
            val piece = board[col][row+i].piece
            if (piece.type == PieceType.ROOK
                    || piece.type == PieceType.QUEEN
                    && piece.color == opponentColor) {
                return true
            }
        }
        for (i in 1..row){
            val piece = board[col][row-i].piece
            if (piece.type == PieceType.ROOK
                || piece.type == PieceType.QUEEN
                && piece.color == opponentColor){
                return true
            }
        }
        val movesRight = 7-col
        for (i in 1..movesRight){
            val piece = board[col+i][row].piece
            if (piece.type == PieceType.ROOK
                || piece.type == PieceType.QUEEN
                && piece.color == opponentColor){
                return true
            }
        }
        for (i in 1..col){
            val piece = board[col-i][row].piece
            if (piece.type == PieceType.ROOK
                || piece.type == PieceType.QUEEN
                && piece.color == opponentColor){
                return true
            }
        }

        val movesUpRight = min(7-row, 7-col)
        for (i in 1..movesUpRight){
            val piece = board[col+i][row+i].piece
            if (piece.type == PieceType.BISHOP||
                piece.type == PieceType.QUEEN
                && piece.color == opponentColor) {
                return true
            }
        }

        val movesUpLeft = min(7-col,row)
        for (i in 1..movesUpLeft){
            val piece = board[col+i][row-i].piece
            if (piece.type == PieceType.BISHOP||
                piece.type == PieceType.QUEEN
                && piece.color == opponentColor) {
                return true
            }
        }

        val movesDownLeft = min(col,row)
        for (i in 1..movesDownLeft) {
            val piece = board[col - i][row - i].piece
            if (piece.type == PieceType.BISHOP ||
                piece.type == PieceType.QUEEN
                && piece.color == opponentColor) {
                return true
            }
        }

        val movesDownRight = min(col,7-row)
        for (i in 1..movesDownRight){
            val piece = board[col-i][row+i].piece
            if (piece.type == PieceType.BISHOP||
                piece.type == PieceType.QUEEN
                && piece.color == opponentColor) {
                return true
            }
            else if (piece.color == ownColor){
                break
            }
            //todo finish + do pin
        }
        return false
    }


    private fun canQueenMove(from: Square, to: Square): Boolean {
        return canRookMove(from, to) || canBishopMove(from, to)
    }

    fun pieceTypeOnSquare(col: Int, row: Int): PieceType {
        return board[col][row].piece.type
    }

    fun generateValidKnightMoves(knightSquare: Square) {
        val col = knightSquare.col
        val row = knightSquare.row
        //(x - 2, y - 1), Pair(x - 2, y + 1), Pair(x - 1, y - 2), Pair(x - 1, y + 2), Pair(x + 2, y - 1), Pair(x + 2, y + 1)
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
                col++
            }
        }
    }
    //todo add castling rights
}

