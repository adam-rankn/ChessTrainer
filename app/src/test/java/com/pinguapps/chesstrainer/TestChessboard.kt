package com.pinguapps.chesstrainer

import com.pinguapps.chesstrainer.data.*
import org.junit.Test

import org.junit.Assert.*

class TestChessboard {
    @Test
    fun testFindSquare() {
        val board = Chessboard()
        val squareA1 = board.getSquare("a1")
        assertEquals(0, squareA1.row)
        assertEquals(0, squareA1.col)

        val squareH8 = board.getSquare("h8")
        assertEquals(7, squareH8.row)
        assertEquals(7, squareH8.col)

        val squareA8 = board.getSquare("a8")
        assertEquals(7, squareA8.row)
        assertEquals(0, squareA8.col)
    }

    @Test
    fun testColor() {
        val board = Chessboard()
        val squareA1 = board.getSquare("a1")
        val squareA2 = board.getSquare("a2")
        val squareA8 = board.getSquare("a8")

        assertEquals(Color.BLACK,squareA1.squareColor)
        assertEquals(Color.WHITE,squareA2.squareColor)
        assertEquals(Color.WHITE,squareA8.squareColor)
    }
    @Test
    fun testInitialPosition(){
        val board = Chessboard()
        val squareA2 = board.getSquare("a2")
        assertEquals(PieceType.PAWN,squareA2.piece.type)
        assertEquals(Color.WHITE,squareA2.piece.color)

        assertEquals(PieceType.ROOK,board.getSquare("h8").piece.type)

        assertEquals(PieceType.BISHOP,board.getSquare("c1").piece.type)
        assertEquals(PieceType.QUEEN,board.getSquare("d1").piece.type)

        assertEquals(PieceType.PAWN,board.getSquare("d7").piece.type)
        assertEquals(Color.BLACK,board.getSquare("d7").piece.color)

        assertEquals(PieceType.BISHOP,board.getSquare("c8").piece.type)
        assertEquals(PieceType.QUEEN,board.getSquare("d8").piece.type)

        assertEquals(PieceType.PAWN,board.getSquare("a7").piece.type)
        assertEquals(PieceType.PAWN,board.getSquare("b7").piece.type)
        assertEquals(PieceType.PAWN,board.getSquare("c7").piece.type)
        assertEquals(PieceType.PAWN,board.getSquare("d7").piece.type)
        assertEquals(PieceType.PAWN,board.getSquare("e7").piece.type)
        assertEquals(PieceType.PAWN,board.getSquare("f7").piece.type)
        assertEquals(PieceType.PAWN,board.getSquare("h7").piece.type)

    }

    @Test
    fun testMakeMove(){
        val board = Chessboard()
        board.makeMoveFromString("e2","e4")

        assertEquals(PieceType.PAWN,board.getSquare("e4").piece.type)
        assertEquals(Color.WHITE,board.getSquare("e4").piece.color)
        assertEquals(PieceType.NONE,board.getSquare("e2").piece.type)

        board.makeMoveFromString("e7","e5")
        assertEquals(PieceType.PAWN,board.getSquare("e5").piece.type)
        assertEquals(Color.BLACK,board.getSquare("e5").piece.color)
        assertEquals(PieceType.NONE,board.getSquare("e7").piece.type)

    }

    @Test
    fun testClearHorizontally(){
        val board = Chessboard()
        val squareA1 = board.getSquare("a1")
        val squareH1 = board.getSquare("h1")

        assertEquals(false,board.isClearHorizontallyBetween(squareA1,squareH1))

        val squareA4 = board.getSquare("a4")
        val squareH4 = board.getSquare("h4")
        assertEquals(true,board.isClearHorizontallyBetween(squareA4,squareH4))

        val squareD1 = board.getSquare("d1")
        val squareE1 = board.getSquare("e1")
        assertEquals(true,board.isClearHorizontallyBetween(squareD1,squareE1))

        board.makeMoveFromString("e2","e4")
        assertEquals(false,board.isClearHorizontallyBetween(squareA4,squareH4))
    }

    @Test
    fun testBishopMove(){
        val board = Chessboard()
        val squareF1 = board.getSquare("f1")
        val squareC4 = board.getSquare("c4")
        val squareC5 = board.getSquare("c5")


        assertEquals(false,board.canBishopMove(squareF1,squareC4))
        board.makeMoveFromString("e2","e4")
        assertEquals(true,board.canBishopMove(squareF1,squareC4))

        board.makeMoveFromString("f1","c4")
        assertEquals(false,board.canBishopMove(squareC4,squareC5))
    }

    @Test
    fun testRookMove(){
        val board = Chessboard()
        board.loadPositionFenString("4k3/4r3/8/8/8/8/4R3/4K3 w - - 0 1")

        val rookStart = board.getSquare("e2")
        val rookTarget = board.getSquare("e7")
        assertEquals(true,board.canRookMove(rookStart,rookTarget))

        val rookTarget3 = board.getSquare("a2")
        //assertEquals(false,board.canRookMove(rookStart,rookTarget3))

        //add knight
        board.loadPositionFenString("4k3/4r3/4N3/8/8/8/4R3/4K3 w - - 0 1")
        val rookTarget2 = board.getSquare("e6")
        val rookTarget2a = board.getSquare("e7")

        //test pin
        assertEquals(false,board.canRookMove(rookStart,rookTarget2))

        assertEquals(false,board.isClearVerticallyBetween(rookStart,rookTarget2a))
        assertEquals(false,board.canRookMove(rookStart,rookTarget2a))

    }

    @Test
    fun testFenString(){
        val board = Chessboard()
        board.loadPositionFenString("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b Kq - 1 2")

        assertEquals(false,board.blackCastleKingRights)
        assertEquals(true,board.blackCastleQueenRights)
        assertEquals(true,board.whiteCastleKingRights)
        assertEquals(false,board.whiteCastleQueenRights)

        assertEquals(PieceType.KNIGHT,board.getSquare("f3").piece.type)

        board.loadPositionFenString("8/3k4/3p4/2pP1p2/1KP2P2/8/8/8")
        assertEquals(PieceType.PAWN,board.getSquare("c4").piece.type)
        assertEquals(PieceType.PAWN,board.getSquare("c5").piece.type)
        assertEquals(PieceType.PAWN,board.getSquare("d5").piece.type)
        assertEquals(PieceType.PAWN,board.getSquare("d6").piece.type)

        assertEquals(Color.WHITE,board.getSquare("c4").piece.color)
        assertEquals(Color.BLACK,board.getSquare("d6").piece.color)

        assertEquals(PieceType.KING,board.getSquare("b4").piece.type)
        assertEquals(PieceType.KING,board.getSquare("d7").piece.type)
    }

    @Test
    fun testGenerateRookMoves(){
        val board = Chessboard()
        board.loadPositionFenString("4k3/4r3/8/8/8/8/4R3/4K3 w - - 0 1")
        board.checkForPins(board.whiteKingSquare)
        board.checkForPins(board.blackKingSquare)

       val movesList = board.generatePieceMoves(board.getSquare("e2"))
        assertEquals(5, movesList.size)

        board.loadPositionFenString("8/8/8/8/3R4/8/8/8 w - - 0 1")
        val movesList2 = board.generateRookMoves(board.getSquare("d4"))
        assertEquals(14, movesList2.size)

        board.loadPositionFenString("6k1/8/8/8/3R2n1/3K4/8/8 w - - 0 1")
        val movesList3 = board.generateRookMoves(board.getSquare("d4"))
        assertEquals(10, movesList3.size)
    }

    @Test
    fun testGenerateValidKnightMoves() {
        val board = Chessboard()
        val moves = board.generateValidKnightMoves(board.getSquare("g1"))
        assertEquals(2, moves.size)

        board.loadPositionFenString("rnbqkbnr/pppppppp/8/8/3N4/8/PP1P1PPP/RNBQKB1R w KQkq - 0 1")
        val moves2 = board.generateValidKnightMoves(board.getSquare("d4"))
        assertEquals(8, moves2.size)

        //knights cannot move if pinned
        board.loadPositionFenString("rnbqk1nr/pppp1ppp/4p3/8/1b6/2NP4/PPP1PPPP/R1BQKBNR w KQkq - 0 1")
        val moves3 = board.generateValidKnightMoves(board.getSquare("c3"))
        assertEquals(0, moves3.size)
    }

    @Test
    fun testCheckPins() {
        val board = Chessboard()
        board.loadPositionFenString("R1rnk3/ppppqnpp/5p2/1B5B/1b2R2b/2NP2P1/PPP2P1P/3QKN1r w - - 0 1")

        //run for black and white kings
        board.checkForPins(board.whiteKingSquare)
        board.checkForPins(board.blackKingSquare)

        //testing all directions
        assertEquals(PinnedState.VERTICAL,board.getPieceOnSquare("e7").pinned)
        assertEquals(PinnedState.VERTICAL,board.getPieceOnSquare("e4").pinned)
        assertEquals(PinnedState.HORIZONTAL,board.getPieceOnSquare("f1").pinned)
        assertEquals(PinnedState.DIAGONALA8H1,board.getPieceOnSquare("c3").pinned)
        assertEquals(PinnedState.DIAGONALA8H1,board.getPieceOnSquare("f7").pinned)
        assertEquals(PinnedState.DIAGONALA1H8,board.getPieceOnSquare("d7").pinned)

        //if two pieces are on the same line neither gets pinned
        assertEquals(PinnedState.NONE,board.getPieceOnSquare("f2").pinned)
        assertEquals(PinnedState.NONE,board.getPieceOnSquare("d3").pinned)

        assertEquals(PinnedState.NONE,board.getPieceOnSquare("c8").pinned)
        assertEquals(PinnedState.NONE,board.getPieceOnSquare("d8").pinned)

        //test king does not get flagged as pinned
        assertEquals(PinnedState.NONE,board.whiteKingSquare.piece.pinned)
        assertEquals(PinnedState.NONE,board.blackKingSquare.piece.pinned)

    }

    @Test
    fun testGeneratePawnMoves(){
        val board = Chessboard()
        board.loadPositionFenString("r1b1kb2/1ppppppp/5N1B/1q2r3/PpP1P3/5n1n/1P2PPPP/RN1QKB1R w KQq a3 0 1")
        board.enPassantSquare = board.getSquare("a3")

        val g2moves = board.generateValidPawnMoves(board.getSquare("g2"))
        assertEquals(4,g2moves.size)

        val g7moves = board.generateValidPawnMoves(board.getSquare("g7"))
        assertEquals(4,g7moves.size)

        val b4moves = board.generateValidPawnMoves(board.getSquare("b4"))
        assertEquals(2,b4moves.size)

        val e4moves = board.generateValidPawnMoves(board.getSquare("e4"))
        assertEquals(0,e4moves.size)

        val d7moves = board.generateValidPawnMoves(board.getSquare("d7"))
        assertEquals(2,d7moves.size)

        board.loadPositionFenString("k2qqq2/1p2Pb2/P1B5/8/6pP/1P4P1/K1P1r3/8 w - - 0 1")
        board.enPassantSquare = board.getSquare("h3")
        val b3moves = board.generateValidPawnMoves(board.getSquare("b3"))
        assertEquals(0,b3moves.size)

        val c2moves = board.generateValidPawnMoves(board.getSquare("c2"))
        assertEquals(0,c2moves.size)

        val g4moves = board.generateValidPawnMoves(board.getSquare("g4"))
        assertEquals(1,g4moves.size)

        val b7moves = board.generateValidPawnMoves(board.getSquare("b7"))
        assertEquals(1,b7moves.size)

        val e7moves = board.generateValidPawnMoves(board.getSquare("e7"))
        assertEquals(2,e7moves.size)

        board.loadPositionFenString("1k2rr2/8/1p6/B1BpP3/8/6b1/5P2/1R2K3 w - - 0 1")
        board.enPassantSquare = board.getSquare("d6")

        val f2moves = board.generateValidPawnMoves(board.getSquare("f2"))
        assertEquals(1,f2moves.size)

        val b6moves = board.generateValidPawnMoves(board.getSquare("b6"))
        assertEquals(1,b6moves.size)

        val e5moves = board.generateValidPawnMoves(board.getSquare("e5"))
        assertEquals(1,e5moves.size)


    }
}