package com.pinguapps.chesstrainer

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pinguapps.chesstrainer.logic.Chessgame
import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.GameResult
import com.pinguapps.chesstrainer.data.PieceType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class TestChessGame {


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

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
        assertEquals(0, game.lastMoves.size)
    }

    @Test
    fun testRedoMove(){
        val game = Chessgame()
        val board = game.chessboard
        board.validMoves = board.generatePieceMoves(board.getSquare("e2"))
        game.makeHumanMove(board.getSquare("e4"))
        board.validMoves = board.generatePieceMoves(board.getSquare("e7"))
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
        assertEquals(2, game.lastMoves.size)
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
        assertEquals(0, game.lastMoves.size)
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
        //todo overhaul tests
    }

    @Test
    fun testPromotePawn(){
        val game = Chessgame()
        val board = game.chessboard
        game.promotePawn(board.getSquare("c1"),PieceType.BISHOP,Color.WHITE)
        assertEquals(PieceType.BISHOP,board.getPieceOnSquare("c1").type)
    }

    @Test
    fun testFiftyMoveCounter(){
        val game = Chessgame()
        assertEquals(0,game.fiftyMoveCounter)
        //pawn push doesn't increment
        game.makeMove("e2e4")
        assertEquals(0,game.fiftyMoveCounter)
        game.makeMove("g8f6")
        assertEquals(1,game.fiftyMoveCounter)
        game.makeMove("b1c3")
        assertEquals(2,game.fiftyMoveCounter)
        game.makeMove("f6d5")
        assertEquals(3,game.fiftyMoveCounter)
        //capture resets
        game.makeMove("c3d5")
        assertEquals(0,game.fiftyMoveCounter)
        game.makeMove("f1e2")
        assertEquals(1,game.fiftyMoveCounter)
        game.makeMove("b8c6")
        assertEquals(2,game.fiftyMoveCounter)
        //pawn push resets
        game.makeMove("a2a3")
        assertEquals(0,game.fiftyMoveCounter)


    }

    @Test
    fun testInsufficientMaterial(){
        val game = Chessgame()
        //  lone knight vs king
        game.loadPositionFenString("1k6/6n1/8/8/8/8/8/1K6 b - - 0 1")
        game.makeMove("g7e8")
        assertEquals(GameResult.DRAW_BY_INSUFFICIENT,game.gameResult.value)
        game.restartGame()

        //single pawn
        game.loadPositionFenString("1k6/p5n1/8/8/8/8/8/1K6 b - - 0 1")
        game.makeMove("g7e8")
        assertEquals(GameResult.GAME_IN_PROGRESS,game.gameResult.value)
        game.restartGame()

        //one bishop on each side
        game.loadPositionFenString("1k6/b7/8/8/8/8/B7/1K6 b - - 0 1")
        game.makeMove("a7b6")
        assertEquals(GameResult.DRAW_BY_INSUFFICIENT,game.gameResult.value)
        game.restartGame()

        //bishop vs knight
        game.loadPositionFenString("1k6/n7/8/8/8/8/B7/1K6 b - - 0 1")
        game.makeMove("a7b6")
        assertEquals(GameResult.DRAW_BY_INSUFFICIENT,game.gameResult.value)
        game.restartGame()

        //king vs king
        game.loadPositionFenString("8/k7/8/8/8/8/K7/8 b - - 0 1")
        game.makeMove("a7b6")
        assertEquals(GameResult.DRAW_BY_INSUFFICIENT,game.gameResult.value)
        game.restartGame()

        //  knight vs knight
        game.loadPositionFenString("1k6/6n1/8/8/8/3N4/8/1K6 w - - 0 1")
        game.makeMove("g7e8")
        assertEquals(GameResult.DRAW_BY_INSUFFICIENT,game.gameResult.value)
        game.restartGame()
    }

    @Test
    fun testThreefoldRep(){
        val game = Chessgame()
        game.makeMove("e2e4")
        game.makeMove("e7e5")

        game.makeMove("e1e2")
        game.makeMove("e8e7")
        game.makeMove("e2e1")
        game.makeMove("e7e8")

        game.makeMove("e1e2")
        game.makeMove("e8e7")
        game.makeMove("e2e1")
        assertEquals(GameResult.GAME_IN_PROGRESS,game.gameResult.value)
        game.makeMove("e7e8")
        assertEquals(GameResult.DRAW_BY_REPETITION,game.gameResult.value)

        game.restartGame()
        game.makeMove("e2e4")
        game.makeMove("e7e5")
        game.makeMove("e1e2")
        game.makeMove("e8e7")
        game.makeMove("e2e1")
        game.makeMove("e7e8")

        game.undoMove()
        game.undoMove()
        game.undoMove()
        game.undoMove()

        game.makeMove("e1e2")
        game.makeMove("e8e7")
        game.makeMove("e2e1")
        game.makeMove("e7e8")

        game.makeMove("e1e2")
        game.makeMove("e8e7")
        game.makeMove("e2e1")
        assertEquals(GameResult.GAME_IN_PROGRESS,game.gameResult.value)
        game.makeMove("e7e8")
        assertEquals(GameResult.DRAW_BY_REPETITION,game.gameResult.value)

        game.restartGame()
        game.makeMove("e2e4")
        game.makeMove("e7e5")

        game.makeMove("e1e2")
        game.makeMove("e8e7")
        game.makeMove("e2e1")
        game.makeMove("e7e8")

        game.makeMove("e1e2")
        game.makeMove("e8e7")
        game.makeMove("e2e1")
        game.undoMove()
        game.undoMove()
        game.undoMove()
        game.redoMove()
        game.redoMove()
        game.redoMove()
        assertEquals(GameResult.GAME_IN_PROGRESS,game.gameResult.value)
        game.makeMove("e7e8")
        assertEquals(GameResult.DRAW_BY_REPETITION,game.gameResult.value)

        game.restartGame()
        game.makeMove("e2e4")
        game.makeMove("e7e5")

        game.makeMove("e1e2")
        game.makeMove("e8e7")
        game.makeMove("e2e1")
        game.makeMove("e7e8")

        game.makeMove("e1e2")
        game.makeMove("e8e7")
        game.makeMove("e2e1")
        game.undoAllMoves()
        game.redoAllMoves()
        assertEquals(GameResult.GAME_IN_PROGRESS,game.gameResult.value)
        game.makeMove("e7e8")
        assertEquals(GameResult.DRAW_BY_REPETITION,game.gameResult.value)
    }

    @Test
    fun testTurns(){
        val game = Chessgame()
        game.playerColor = Color.WHITE
        assert(game.isPlayerTurn())
        assertFalse(game.isCpuTurn())
        game.makeMove("e2e4")
        assert(game.isCpuTurn())
        assertFalse(game.isPlayerTurn())
        game.makeMove("e7e5")

        game.restartGame()
        game.playerColor = Color.BLACK
        assert(game.isCpuTurn())
        assertFalse(game.isPlayerTurn())
        game.makeMove("e2e4")
        assert(game.isPlayerTurn())
        assertFalse(game.isCpuTurn())

        //change player color mid game
        game.playerColor = Color.WHITE
        assert(game.isCpuTurn())
        assertFalse(game.isPlayerTurn())
        game.makeMove("e7e5")



    }
}