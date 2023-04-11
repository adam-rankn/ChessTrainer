package com.pinguapps.chesstrainer.logic.bots

import com.pinguapps.chesstrainer.engine.cuckoo.chess.ComputerPlayer

abstract class Bot {

    abstract val description: String
    abstract val name: String
    abstract val computer: ComputerPlayer


    abstract fun getMove(fen: String): String


}