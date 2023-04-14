package com.pinguapps.chesstrainer.data.remote

import android.util.Log
import com.pinguapps.chesstrainer.data.LichessDbMove
import com.pinguapps.chesstrainer.data.PieceType

/**
 *
 * formats the data from the lichess api
 *
 */
fun getLinesData(data: LichessOpeningData): List<LichessDbMove> {
    val movesList = data.moves
    try {
        val totalGames = data.white!! + data.black!! + data.draws!!

        val lines = mutableListOf<LichessDbMove>()
        if (totalGames > 0) {
            for (move in movesList) {
                val white = move.white!!
                val black = move.black!!
                val draws = move.draws!!
                val games = white + black + draws
                val whiteWinPercent: Float = white / games.toFloat()
                val blackWinPercent: Float = black / games.toFloat()
                val drawPercent: Float = draws / games.toFloat()
                val playedPercent = games / totalGames.toFloat()

                //change weird lichess castling notation to standard uci
                val uci: String =
                    if (move.san == "O-O" || move.san == "O-O-O") {
                        when (move.uci) {
                            "e1h1" -> "e1g1c"
                            "e1a1" -> "e1c1c"
                            "e8h8" -> "e8g8c"
                            "e8a8" -> "e8c8c"
                            else -> {
                                move.uci!!
                            }
                        }
                    } else {
                        move.uci!!
                    }

                val lichessMove = LichessDbMove(
                    uci = uci,
                    whiteWinPercent = whiteWinPercent,
                    blackWinPercent = blackWinPercent,
                    drawPercent = drawPercent,
                    totalGames = games,
                    playedPercent = playedPercent,
                    san = move.san!!
                )
                lines.add(lichessMove)
            }

            return lines
        } else {
            return listOf()
        }

    } catch (e: java.lang.NullPointerException) {
        Log.e("API error", "bad data")
        return listOf()
    }
}