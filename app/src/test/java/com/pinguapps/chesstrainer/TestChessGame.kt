package com.pinguapps.chesstrainer

import com.pinguapps.chesstrainer.data.Chessgame
import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.PieceType
import org.junit.Assert.assertEquals
import org.junit.Test

class TestChessGame {

    @Test
    fun testLoadFenString(){
        val game = Chessgame()
        val board = game.chessboard
        game.loadPositionFenString("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b Kq - 7 23")

        assertEquals(PieceType.KNIGHT, board.getSquare("f3").pieceType)

        board.loadPositionFenString("8/3k4/3p4/2pP1p2/1KP2P2/8/8/8 w - - 0 1")
        assertEquals(PieceType.PAWN, board.getSquare("c4").pieceType)
        assertEquals(PieceType.PAWN, board.getSquare("c5").pieceType)
        assertEquals(PieceType.PAWN, board.getSquare("d5").pieceType)
        assertEquals(PieceType.PAWN, board.getSquare("d6").pieceType)

        assertEquals(Color.WHITE, board.getSquare("c4").pieceColor)
        assertEquals(Color.BLACK, board.getSquare("d6").pieceColor)

        assertEquals(PieceType.KING, board.getSquare("b4").pieceType)
        assertEquals(PieceType.KING, board.getSquare("d7").pieceType)
        assertEquals(7,game.fiftyMoveCounter)
        assertEquals(23, game.moveCounter)
    }

    @Test
    fun testUndoMove(){
        val game = Chessgame()
        val board = game.chessboard
        board.validMoves = board.generatePieceMoves(board.getSquare("e2"))
        game.makeHumanMove(board.getSquare("e4"))
        board.validMoves = board.generatePieceMoves(board.getSquare("e7"))
        //board.selectedSquare = board.getSquare("e7")
        game.makeHumanMove(board.getSquare("e5"))
        assertEquals("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2",
            game.generateFenStringFromPosition())
        game.undoMove()
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
            game.generateFenStringFromPosition())
        game.undoMove()
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            game.generateFenStringFromPosition())
    }

    @Test
    fun testRedoMove(){
        val game = Chessgame()
        val board = game.chessboard
        board.validMoves = board.generatePieceMoves(board.getSquare("e2"))
        game.makeHumanMove(board.getSquare("e4"))
        board.validMoves = board.generatePieceMoves(board.getSquare("e7"))
        //board.selectedSquare = board.getSquare("e7")
        game.makeHumanMove(board.getSquare("e5"))
        assertEquals("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2",
            game.generateFenStringFromPosition())
        game.undoMove()
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
            game.generateFenStringFromPosition())
        game.undoMove()
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            game.generateFenStringFromPosition())
        game.redoMove()
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
            game.generateFenStringFromPosition())
        game.redoMove()
        assertEquals("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2",
            game.generateFenStringFromPosition())
    }

    @Test
    fun testUndoAllMoves(){
        val game = Chessgame()
        val board = game.chessboard
        board.validMoves = board.generatePieceMoves(board.getSquare("e2"))
        game.makeHumanMove(board.getSquare("e4"))
        board.validMoves = board.generatePieceMoves(board.getSquare("e7"))
        //board.selectedSquare = board.getSquare("e7")
        game.makeHumanMove(board.getSquare("e5"))
        game.undoAllMoves()
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            game.generateFenStringFromPosition())
    }

    @Test
    fun testRedoAllMoves(){
        val game = Chessgame()
        val board = game.chessboard
        board.validMoves = board.generatePieceMoves(board.getSquare("e2"))
        game.makeHumanMove(board.getSquare("e4"))
        board.validMoves = board.generatePieceMoves(board.getSquare("e7"))
        //board.selectedSquare = board.getSquare("e7")
        game.makeHumanMove(board.getSquare("e5"))
        game.undoAllMoves()
        game.redoAllMoves()
        assertEquals("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2",
            game.generateFenStringFromPosition())
    }
}