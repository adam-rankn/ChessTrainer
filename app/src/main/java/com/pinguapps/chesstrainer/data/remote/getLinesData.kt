package com.pinguapps.chesstrainer.data.remote

import android.util.Log
import com.pinguapps.chesstrainer.data.LichessDbMove

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
                val whiteWinPercent: Float = white/games.toFloat()
                val blackWinPercent: Float = black/games.toFloat()
                val drawPercent: Float = draws/games.toFloat()
                val playedPercent = games/totalGames.toFloat()

                val lichessMove = LichessDbMove(
                    uci = move.uci!!,
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
        }
        else {
            return listOf()
        }


    }
    catch (e: java.lang.NullPointerException){
        Log.e("API error","bad data")
        return listOf()
        //todo
    }
}