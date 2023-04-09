package com.pinguapps.chesstrainer.data



class LichessDbMove(
    val totalGames: Int = 0,
    val playedPercent: Float = 0f,
    val whiteWinPercent: Float = 0f,
    val blackWinPercent: Float = 0f,
    val drawPercent: Float = 0f,
    val uci: String = "",
    val san: String = ""
)