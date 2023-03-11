package com.pinguapps.chesstrainer.logic

import com.pinguapps.chesstrainer.data.Chessboard
import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.Piece
import com.pinguapps.chesstrainer.data.PieceType
import com.pinguapps.chesstrainer.util.randomFromTwo
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class PassedPawnPuzzle {

    val chessboard = Chessboard()
    var player: Color = Color.WHITE
    var opponent: Color = Color.BLACK
    private val randomGenerator = Random(System.currentTimeMillis())


    init {
        chessboard.clearBoard()
        if ((0..1).random() < 1){
            player = Color.WHITE
            opponent = Color.BLACK
        }
        else {
            player = Color.BLACK
            opponent = Color.WHITE
        }
    }

    private fun addFiller(col: Int){
        if (col > 4){
            chessboard.board[1][7].piece = Piece(player, PieceType.KING)
            chessboard.whiteKingSquare = chessboard.board[1][7]
            chessboard.board[1][0].piece = Piece(opponent, PieceType.KING)
            chessboard.blackKingSquare = chessboard.board[1][0]

            if(randomGenerator.nextInt(1,10) >= 2){
                chessboard.board[0][1].piece = Piece(opponent, PieceType.PAWN)
            }
            if(randomGenerator.nextInt(1,10) >= 4){
                chessboard.board[1][1].piece = Piece(opponent, PieceType.PAWN)
                }
            if(randomGenerator.nextInt(1,10) >= 7){
                chessboard.board[2][1].piece = Piece(opponent, PieceType.PAWN)
            }

            if(randomGenerator.nextInt(1,10) >= 2){
                chessboard.board[0][6].piece = Piece(player, PieceType.PAWN)
            }
            if(randomGenerator.nextInt(1,10) >= 4){
                chessboard.board[1][6].piece = Piece(player, PieceType.PAWN)
            }
            if(randomGenerator.nextInt(1,10) >= 7){
                chessboard.board[2][6].piece = Piece(player, PieceType.PAWN)
            }
            //todo per colum
        }
        else if (col < 4){
            chessboard.board[6][7].piece = Piece(player, PieceType.KING)
            chessboard.whiteKingSquare = chessboard.board[6][7]
            chessboard.board[6][0].piece = Piece(opponent, PieceType.KING)
            chessboard.blackKingSquare = chessboard.board[6][0]

            if(randomGenerator.nextInt(1,10) >= 2){
                chessboard.board[7][1].piece = Piece(opponent, PieceType.PAWN)
            }
            if(randomGenerator.nextInt(1,10) >= 4){
                chessboard.board[6][1].piece = Piece(opponent, PieceType.PAWN)
            }
            if(randomGenerator.nextInt(1,10) >= 7){
                chessboard.board[5][1].piece = Piece(opponent, PieceType.PAWN)
            }

            if(randomGenerator.nextInt(1,10) >= 2){
                chessboard.board[7][6].piece = Piece(player, PieceType.PAWN)
            }
            if(randomGenerator.nextInt(1,10) >= 4){
                chessboard.board[6][6].piece = Piece(player, PieceType.PAWN)
            }
            if(randomGenerator.nextInt(1,10) >= 7){
                chessboard.board[5][6].piece = Piece(player, PieceType.PAWN)
            }
        }
    }

    fun generateBasicPuzzle(){

        randomGenerator.nextInt(1,2)
        val colsList = listOf(0,1,2,5,6,7)
        val col = colsList[randomGenerator.nextInt(0,5)]
        addFiller(col)
        if (player == Color.WHITE){
             val row = (1..2).random()
             chessboard.board[col][row].piece = Piece(opponent,PieceType.PAWN)
             chessboard.board[col][row+1].piece = Piece(player,PieceType.PAWN)
             when (col) {
                 in 1..6 -> {
                     val dx = randomFromTwo(-1, 1)
                     chessboard.board[col + dx][row + 2].piece = Piece(player, PieceType.PAWN)
                 }
                 0 -> {
                     chessboard.board[1][row + 2].piece = Piece(player, PieceType.PAWN)
                 }
                 7 -> {
                     chessboard.board[6][row + 2].piece = Piece(player, PieceType.PAWN)
                 }

             }

         }
        else {
             val row = (5..6).random()
        }



    }
}