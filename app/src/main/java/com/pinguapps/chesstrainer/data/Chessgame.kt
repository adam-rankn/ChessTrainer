package com.pinguapps.chesstrainer.data

import java.util.Stack

class Chessgame(color: Color= Color.WHITE) {

    val chessboard: Chessboard = Chessboard()

    val playerColor = color

    val moveHistory: Stack<Move> = Stack<Move>()
    //todo add moves to stack
    var selectedSquare : Square? = null
    var validMoves = mutableListOf<Move>()

    var whiteCastleQueenRights = true
    var whiteCastleKingRights  = true
    var blackCastleQueenRights = true
    var blackCastleKingRights  = true
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
                chessboard.board[col][row].piece = Piece(color,type!!)
                if (character == 'k') {
                    chessboard.blackKingSquare = chessboard.board[col][row]
                }
                if (character == 'K') {
                    chessboard.whiteKingSquare = chessboard.board[col][row]

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

        //todo
        chessboard.checkForPins(chessboard.blackKingSquare)
        chessboard.checkForPins(chessboard.whiteKingSquare)
    }
    //todo en passant
    //todo 50 move counter/half moves
    //todo full move ctr

    fun clearBoard() {
        for (col in chessboard.board){
            for (square in col){
                square.piece = Piece(Color.NONE,PieceType.NONE)
            }
        }
        whiteCastleQueenRights = false
        whiteCastleKingRights  = false
        blackCastleQueenRights = false
        blackCastleKingRights  = false
    }
}