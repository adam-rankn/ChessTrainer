package com.pinguapps.chesstrainer.data



class LichessDbMove(
    val totalGames: Int,
    val playedPercent: Float,
    val whiteWinPercent: Float,
    val blackWinPercent: Float,
    val drawPercent: Float,
    val uci: String,
    val san: String
)