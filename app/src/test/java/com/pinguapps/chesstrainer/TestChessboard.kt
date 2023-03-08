package com.pinguapps.chesstrainer

import com.pinguapps.chesstrainer.data.*
import org.junit.Test

import org.junit.Assert.*

class TestChessboard {
    @Test
    fun testFindSquare() {
        val board = Chessboard()
        val squareA1 = board.getSquare("a1")
        assertEquals(7, squareA1.row)
        assertEquals(0, squareA1.col)

        val squareH8 = board.getSquare("h8")
        assertEquals(0, squareH8.row)
        assertEquals(7, squareH8.col)

        val squareA8 = board.getSquare("a8")
        assertEquals(0, squareA8.row)
        assertEquals(0, squareA8.col)
    }

    @Test
    fun testColor() {
        val board = Chessboard()
        val squareA1 = board.getSquare("a1")
        val squareA2 = board.getSquare("a2")
        val squareA8 = board.getSquare("a8")

        assertEquals(Color.WHITE,squareA1.squareColor)
        assertEquals(Color.WHITE,squareA2.squareColor)
        assertEquals(Color.BLACK,squareA8.squareColor)
    }
    @Test
    fun testInitialPosition(){
        val board = Chessboard()
        val squareA2 = board.getSquare("a2")
        assertEquals(PieceType.PAWN,squareA2.pieceType)
        assertEquals(Color.WHITE,squareA2.pieceColor)

        assertEquals(PieceType.ROOK,board.getSquare("h8").pieceType)

        assertEquals(PieceType.BISHOP,board.getSquare("c1").pieceType)
        assertEquals(PieceType.QUEEN,board.getSquare("d1").pieceType)

        assertEquals(PieceType.PAWN,board.getSquare("d7").pieceType)
        assertEquals(Color.BLACK,board.getSquare("d7").pieceColor)

        assertEquals(PieceType.BISHOP,board.getSquare("c8").pieceType)
        assertEquals(PieceType.QUEEN,board.getSquare("d8").pieceType)

        assertEquals(PieceType.PAWN,board.getSquare("a7").pieceType)
        assertEquals(PieceType.PAWN,board.getSquare("b7").pieceType)
        assertEquals(PieceType.PAWN,board.getSquare("c7").pieceType)
        assertEquals(PieceType.PAWN,board.getSquare("d7").pieceType)
        assertEquals(PieceType.PAWN,board.getSquare("e7").pieceType)
        assertEquals(PieceType.PAWN,board.getSquare("f7").pieceType)
        assertEquals(PieceType.PAWN,board.getSquare("h7").pieceType)

    }

    @Test
    fun testMakeMove(){
        val board = Chessboard()
        board.makeMoveFromString("e2","e4")

        assertEquals(PieceType.PAWN,board.getSquare("e4").pieceType)
        assertEquals(Color.WHITE,board.getSquare("e4").pieceColor)
        assertEquals(PieceType.NONE,board.getSquare("e2").pieceType)

        board.makeMoveFromString("e7","e5")
        assertEquals(PieceType.PAWN,board.getSquare("e5").pieceType)
        assertEquals(Color.BLACK,board.getSquare("e5").pieceColor)
        assertEquals(PieceType.NONE,board.getSquare("e7").pieceType)

    }

    @Test
    fun testFenString(){
        val board = Chessboard()
        board.loadPositionFenString("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b Kq - 1 2")

        assertEquals(false,board.blackCastleKingRights)
        assertEquals(true,board.blackCastleQueenRights)
        assertEquals(true,board.whiteCastleKingRights)
        assertEquals(false,board.whiteCastleQueenRights)

        assertEquals(PieceType.KNIGHT,board.getSquare("f3").pieceType)

        board.loadPositionFenString("8/3k4/3p4/2pP1p2/1KP2P2/8/8/8")
        assertEquals(PieceType.PAWN,board.getSquare("c4").pieceType)
        assertEquals(PieceType.PAWN,board.getSquare("c5").pieceType)
        assertEquals(PieceType.PAWN,board.getSquare("d5").pieceType)
        assertEquals(PieceType.PAWN,board.getSquare("d6").pieceType)

        assertEquals(Color.WHITE,board.getSquare("c4").pieceColor)
        assertEquals(Color.BLACK,board.getSquare("d6").pieceColor)

        assertEquals(PieceType.KING,board.getSquare("b4").pieceType)
        assertEquals(PieceType.KING,board.getSquare("d7").pieceType)
    }

    @Test
    fun testGenerateRookMoves(){
        val board = Chessboard()
        board.loadPositionFenString("4k3/4r3/8/8/8/8/4R3/4K3 w - - 0 1")

       val movesList = board.generatePieceMoves(board.getSquare("e2"))
        for (move in movesList){
            println(move.endSquare.row)
        }
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

        //test pawns on edge of board
        board.loadPositionFenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
        val a2moves = board.generateValidPawnMoves(board.getSquare("a2"))
        val h2moves = board.generateValidPawnMoves(board.getSquare("h2"))
        val a7moves = board.generateValidPawnMoves(board.getSquare("a7"))
        val h7moves = board.generateValidPawnMoves(board.getSquare("h7"))
        assertEquals(2,a2moves.size)
        assertEquals(2,a7moves.size)
        assertEquals(2,h2moves.size)
        assertEquals(2,h7moves.size)

        //test captures
        board.loadPositionFenString("rnbqkbnr/8/8/pppppppp/PPPPPPPP/8/8/RNBQKBNR w KQkq - 0 1")
        assertEquals(1,board.generateValidPawnMoves(board.getSquare("a4")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("b4")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("c4")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("d4")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("e4")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("f4")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("g4")).size)
        assertEquals(1,board.generateValidPawnMoves(board.getSquare("h4")).size)
        assertEquals(1,board.generateValidPawnMoves(board.getSquare("a5")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("b5")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("c5")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("d5")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("e5")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("f5")).size)
        assertEquals(2,board.generateValidPawnMoves(board.getSquare("g5")).size)
        assertEquals(1,board.generateValidPawnMoves(board.getSquare("h5")).size)
    }

    @Test
    fun testGenerateBishopMoves(){
        val board = Chessboard()
        board.loadPositionFenString("1kb4R/b7/Rb4q1/8/q7/1B1B1N1N/2K3B1/8 w - - 0 1")

        //diagonal pin
        val d3moves = board.generateBishopMoves(board.getSquare("d3"))
        for (move in d3moves)
            println("${move.endSquare.col} ${move.endSquare.row}")
        assertEquals(3, d3moves.size)

        //horizontal pin
        val c8moves = board.generateBishopMoves(board.getSquare("c8"))
        assertEquals(0, c8moves.size)

        //own piece block
        val g2moves = board.generateBishopMoves(board.getSquare("g2"))
        assertEquals(2, g2moves.size)

        val b3moves = board.generateBishopMoves(board.getSquare("b3"))
        assertEquals(1, b3moves.size)

        val b6moves = board.generateBishopMoves(board.getSquare("b6"))
        assertEquals(8, b6moves.size)

        val a7moves = board.generateBishopMoves(board.getSquare("a7"))
        assertEquals(0, a7moves.size)
    }

    @Test
    fun testGenerateQueenMoves(){
        val board = Chessboard()
        board.loadPositionFenString("4k3/2b1q1q1/8/N7/3n4/2Q5/4R3/r7 w - - 0 1")

        val e7moves = board.generateQueenMoves(board.getSquare("e7"))
        assertEquals(5,e7moves.size)

        val c3moves = board.generateQueenMoves(board.getSquare("c3"))
        assertEquals(19,c3moves.size)

        val g7moves = board.generateQueenMoves(board.getSquare("g7"))
        assertEquals(14,g7moves.size)
    }

    @Test
    fun testIsKingInCheck(){
        val board = Chessboard()
        board.loadPositionFenString("rnbqkb1r/ppp1pppp/3p4/1B6/8/5n2/PPPPPPPP/RNBQK1NR w KQkq - 0 1")
        assertEquals(true,board.isKingInCheck(board.getSquare("e1")))
        assertEquals(true,board.isKingInCheck(board.getSquare("e8")))

        board.loadPositionFenString("5k2/6P1/8/8/qq6/qqq5/PPq5/KPq5 w - - 0 1")
        assertEquals(false,board.isKingInCheck(board.getSquare("a1")))
        assertEquals(true,board.isKingInCheck(board.getSquare("f8")))

        board.loadPositionFenString("bk1r1k1k/8/4N3/2k3k1/8/8/1p6/K2K3K w - - 0 1")
        assertEquals(true,board.isKingInCheck(board.getSquare("a1")))
        assertEquals(true,board.isKingInCheck(board.getSquare("d1")))
        assertEquals(true,board.isKingInCheck(board.getSquare("h1")))
        assertEquals(false,board.isKingInCheck(board.getSquare("b8")))
        assertEquals(true,board.isKingInCheck(board.getSquare("f8")))
        assertEquals(false,board.isKingInCheck(board.getSquare("h8")))
        assertEquals(true,board.isKingInCheck(board.getSquare("c5")))
        assertEquals(true,board.isKingInCheck(board.getSquare("g5")))

        board.loadPositionFenString("k6k/kP4Pk/1P4P1/8/8/1p4p1/Kp4pK/K6K w - - 0 1")
        assertEquals(true,board.isKingInCheck(board.getSquare("a1")))
        assertEquals(true,board.isKingInCheck(board.getSquare("a2")))
        assertEquals(true,board.isKingInCheck(board.getSquare("h1")))
        assertEquals(true,board.isKingInCheck(board.getSquare("h2")))
        assertEquals(true,board.isKingInCheck(board.getSquare("a7")))
        assertEquals(true,board.isKingInCheck(board.getSquare("a8")))
        assertEquals(true,board.isKingInCheck(board.getSquare("h7")))
        assertEquals(true,board.isKingInCheck(board.getSquare("h8")))
    }

    @Test
    fun testGenerateKingMoves() {
        val board = Chessboard()
        board.loadPositionFenString("r3k1nr/pp2pppp/1p1p4/6b1/6B1/3P1NB1/PPP1PPPP/RN1QK2R w KQkq - 0 1")
        val e1moves = board.generateKingMoves(board.getSquare("e1"))
        assertEquals(2,e1moves.size)
        val e8moves = board.generateKingMoves(board.getSquare("e8"))
        assertEquals(2,e8moves.size)

        board.loadPositionFenString("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1")
        val whiteCastleBoth = board.generateKingMoves(board.getSquare("e1"))
        assertEquals(4,whiteCastleBoth.size)
        val blackCastleBoth = board.generateKingMoves(board.getSquare("e8"))
        assertEquals(4,blackCastleBoth.size)

        board.loadPositionFenString("1n2r3/7r/3K1n2/2b5/7B/6B1/Q6k/kQ6 w - - 0 1")
        val a1moves = board.generateKingMoves(board.getSquare("a1"))
        assertEquals(0,a1moves.size)
        val d6moves = board.generateKingMoves(board.getSquare("d6"))
        assertEquals(1,d6moves.size)
        val h2moves = board.generateKingMoves(board.getSquare("h2"))
        assertEquals(1,h2moves.size)

        board.loadPositionFenString("K6K/q6q/8/8/8/1P4P1/1P1RR1P1/k2k3k w - - 0 1")
        val d1moves = board.generateKingMoves(board.getSquare("d1"))
        assertEquals(1,d1moves.size)
        val h1moves = board.generateKingMoves(board.getSquare("h1"))
        assertEquals(2,h1moves.size)
        val a8moves = board.generateKingMoves(board.getSquare("a8"))
        assertEquals(0,a8moves.size)
        val a1moves2 = board.generateKingMoves(board.getSquare("a1"))
        assertEquals(2,a1moves2.size)
        val h8moves = board.generateKingMoves(board.getSquare("h8"))
        assertEquals(0,h8moves.size)

        board.loadPositionFenString("r3k2r/p2p1p1p/p2B1B1p/p6p/P6P/P2b1b1P/P2P1P1P/R3K2R w KQkq - 0 1")
        val blackMoves = board.generateKingMoves(board.getSquare("e8"))
        val whiteMoves = board.generateKingMoves(board.getSquare("e1"))
        assertEquals(0, whiteMoves.size)
        assertEquals(0, blackMoves.size)

        board.loadPositionFenString("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w - - 0 1")
        val blackMoves2 = board.generateKingMoves(board.getSquare("e8"))
        val whiteMoves2 = board.generateKingMoves(board.getSquare("e1"))
        assertEquals(2, whiteMoves2.size)
        assertEquals(2, blackMoves2.size)
    }

    @Test
    fun testCastles() {
        val board = Chessboard()
        board.loadPositionFenString("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1")
        board.validMoves = board.generateKingMoves(board.whiteKingSquare)
        board.selectedSquare = board.getSquare("e1")
        board.makeMove(board.getSquare("g1"))
        assertEquals(PieceType.ROOK, board.getSquare("f1").pieceType)
        assertEquals(PieceType.NONE, board.getSquare("h1").pieceType)
        assertEquals(PieceType.KING, board.getSquare("g1").pieceType)

        board.validMoves = board.generateKingMoves(board.blackKingSquare)
        board.selectedSquare = board.getSquare("e8")
        board.makeMove(board.getSquare("g8"))
        assertEquals(PieceType.ROOK, board.getSquare("f8").pieceType)
        assertEquals(PieceType.NONE, board.getSquare("h8").pieceType)
        assertEquals(PieceType.KING, board.getSquare("g8").pieceType)

        board.loadPositionFenString("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1")
        board.validMoves = board.generateKingMoves(board.whiteKingSquare)
        board.selectedSquare = board.getSquare("e1")
        board.makeMove(board.getSquare("c1"))
        assertEquals(PieceType.ROOK, board.getSquare("d1").pieceType)
        assertEquals(PieceType.NONE, board.getSquare("a1").pieceType)
        assertEquals(PieceType.KING, board.getSquare("c1").pieceType)

        board.validMoves = board.generateKingMoves(board.blackKingSquare)
        board.selectedSquare = board.getSquare("e8")
        board.makeMove(board.getSquare("c8"))
        assertEquals(PieceType.ROOK, board.getSquare("d8").pieceType)
        assertEquals(PieceType.NONE, board.getSquare("a8").pieceType)
        assertEquals(PieceType.KING, board.getSquare("c8").pieceType)
    }

    @Test
    fun testGenerateFenString(){
        val board = Chessboard()
        val initialFenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        assertEquals(initialFenString,board.getFenStringFromPosition())

        val anotherFenString = "r3k1nr/pp2pppp/1p1p4/6b1/6B1/3P1NB1/PPP1PPPP/RN1QK2R w KQkq - 0 1"

        board.loadPositionFenString(anotherFenString)
        assertEquals(anotherFenString,board.getFenStringFromPosition())

        val fenString2Kings = "8/8/8/4k3/8/4K3/8/8 w - - 0 1"
        board.loadPositionFenString(fenString2Kings)
        assertEquals(fenString2Kings,board.getFenStringFromPosition())
    }

    @Test
    fun testLimitMovesWhenInCheck(){
        val board = Chessboard()
        board.loadPositionFenString("rnbqkbnr/ppp2ppp/8/1B1pp3/3PP3/8/PPP2PPP/RNBQK1NR w KQkq - 0 1")
        assertEquals(true,board.isKingInCheck(board.blackKingSquare))

        //val queenMoves = board.generatePieceMoves(board.getSquare("d8"))
        //assertEquals(1,queenMoves.size)
    }

    @Test
    fun testEnPassantCapture(){
        val board = Chessboard()
        board.loadPositionFenString("rnbqkbnr/ppp2ppp/3p4/3Pp3/8/8/PPP1PPPP/RNBQKBNR w KQkq - 0 1")
        board.enPassantSquare = board.getSquare("e6")
        val moves = board.generatePieceMoves(board.getSquare("d5"))
        assertNotNull(moves[0].enPassantSquare)
        board.makeMove(board.getSquare("e6"))
       // assertEquals(PieceType.NONE,board.getSquare("e5").pieceType)
    }
}