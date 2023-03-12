package com.pinguapps.chesstrainer.logic.bots

abstract class Bot {

    abstract val description: String
    abstract val name: String


    abstract fun makeMove()
}