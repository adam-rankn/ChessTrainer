package com.pinguapps.chesstrainer.util

import com.pinguapps.chesstrainer.engine.cuckoo.chess.*

@Throws(ChessParseError::class)
fun fenToEnginePos(fen: String): Position {
    val pos = Position()
    val words = fen.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray()
    if (words.size < 2) {
        throw ChessParseError("Too few spaces")
    }

    // Piece placement
    var row = 7
    var col = 0
    for (i in 0 until words[0].length) {
        when (words[0][i]) {
            '1' -> col += 1
            '2' -> col += 2
            '3' -> col += 3
            '4' -> col += 4
            '5' -> col += 5
            '6' -> col += 6
            '7' -> col += 7
            '8' -> col += 8
            '/' -> {
                row--
                col = 0
            }
            'P' -> {
                safeSetPiece(pos, col, row, Piece.WPAWN)
                col++
            }
            'N' -> {
                safeSetPiece(pos, col, row, Piece.WKNIGHT)
                col++
            }
            'B' -> {
                safeSetPiece(pos, col, row, Piece.WBISHOP)
                col++
            }
            'R' -> {
                safeSetPiece(pos, col, row, Piece.WROOK)
                col++
            }
            'Q' -> {
                safeSetPiece(pos, col, row, Piece.WQUEEN)
                col++
            }
            'K' -> {
                safeSetPiece(pos, col, row, Piece.WKING)
                col++
            }
            'p' -> {
                safeSetPiece(pos, col, row, Piece.BPAWN)
                col++
            }
            'n' -> {
                safeSetPiece(pos, col, row, Piece.BKNIGHT)
                col++
            }
            'b' -> {
                safeSetPiece(pos, col, row, Piece.BBISHOP)
                col++
            }
            'r' -> {
                safeSetPiece(pos, col, row, Piece.BROOK)
                col++
            }
            'q' -> {
                safeSetPiece(pos, col, row, Piece.BQUEEN)
                col++
            }
            'k' -> {
                safeSetPiece(pos, col, row, Piece.BKING)
                col++
            }
            else -> throw ChessParseError("Invalid piece")
        }
    }
    if (words[1].isEmpty()) {
        throw ChessParseError("Invalid side")
    }
    pos.setWhiteMove(words[1][0] == 'w')

    // Castling rights
    var castleMask = 0
    if (words.size > 2) {
        for (i in 0 until words[2].length) {
            when (words[2][i]) {
                'K' -> castleMask = castleMask or (1 shl Position.H1_CASTLE)
                'Q' -> castleMask = castleMask or (1 shl Position.A1_CASTLE)
                'k' -> castleMask = castleMask or (1 shl Position.H8_CASTLE)
                'q' -> castleMask = castleMask or (1 shl Position.A8_CASTLE)
                '-' -> {}
                else -> throw ChessParseError("Invalid castling flags")
            }
        }
    }
    pos.castleMask = castleMask
    if (words.size > 3) {
        // En passant target square
        val epString = words[3]
        if (epString != "-") {
            if (epString.length < 2) {
                throw ChessParseError("Invalid en passant square")
            }
            pos.epSquare = TextIO.getSquare(epString)
        }
    }
    try {
        if (words.size > 4) {
            pos.halfMoveClock = words[4].toInt()
        }
        if (words.size > 5) {
            pos.fullMoveCounter = words[5].toInt()
        }
    } catch (nfe: NumberFormatException) {
        // Ignore errors here, since the fields are optional
    }

    // Each side must have exactly one king
    var wKings = 0
    var bKings = 0
    for (x in 0..7) {
        for (y in 0..7) {
            val p = pos.getPiece(Position.getSquare(x, y))
            if (p == Piece.WKING) {
                wKings++
            } else if (p == Piece.BKING) {
                bKings++
            }
        }
    }
    if (wKings != 1) {
        throw ChessParseError("White must have exactly one king")
    }
    if (bKings != 1) {
        throw ChessParseError("Black must have exactly one king")
    }

    // Make sure king can not be captured
    val pos2 = Position(pos)
    pos2.setWhiteMove(!pos.whiteMove)
    if (MoveGen.inCheck(pos2)) {
        //throw ChessParseError("King capture possible")
    }
    TextIO.fixupEPSquare(pos)
    return pos
}

@Throws(ChessParseError::class)
private fun safeSetPiece(pos: Position, col: Int, row: Int, p: Int) {
    if (row < 0) throw ChessParseError("Too many rows")
    if (col > 7) throw ChessParseError("Too many columns")
    if (p == Piece.WPAWN || p == Piece.BPAWN) {
        if (row == 0 || row == 7) throw ChessParseError("Pawn on first/last rank")
    }
    pos.setPiece(Position.getSquare(col, row), p)
}