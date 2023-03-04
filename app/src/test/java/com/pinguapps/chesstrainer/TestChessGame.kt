package com.pinguapps.chesstrainer

import com.pinguapps.chesstrainer.data.Chessgame
import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.PieceType
import org.junit.Assert
import org.junit.Test

class TestChessGame {

    @Test
    fun testFenString(){
        val game = Chessgame()
        val board = game.chessboard
        game.loadPositionFenString("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b Kq - 1 2")

        Assert.assertEquals(false, game.blackCastleKingRights)
        Assert.assertEquals(true, game.blackCastleQueenRights)
        Assert.assertEquals(true, game.whiteCastleKingRights)
        Assert.assertEquals(false, game.whiteCastleQueenRights)

        Assert.assertEquals(PieceType.KNIGHT, board.getSquare("f3").pieceType)

        board.loadPositionFenString("8/3k4/3p4/2pP1p2/1KP2P2/8/8/8")
        Assert.assertEquals(PieceType.PAWN, board.getSquare("c4").pieceType)
        Assert.assertEquals(PieceType.PAWN, board.getSquare("c5").pieceType)
        Assert.assertEquals(PieceType.PAWN, board.getSquare("d5").pieceType)
        Assert.assertEquals(PieceType.PAWN, board.getSquare("d6").pieceType)

        Assert.assertEquals(Color.WHITE, board.getSquare("c4").pieceColor)
        Assert.assertEquals(Color.BLACK, board.getSquare("d6").pieceColor)

        Assert.assertEquals(PieceType.KING, board.getSquare("b4").pieceType)
        Assert.assertEquals(PieceType.KING, board.getSquare("d7").pieceType)
    }
}